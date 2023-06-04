package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.api.interfaces.ChipstackPayload;
import com.disk91.forwarder.api.interfaces.HeliumDownlink;
import com.disk91.forwarder.api.interfaces.HeliumPayload;
import com.disk91.forwarder.api.interfaces.sub.*;
import com.disk91.forwarder.service.itf.ChirpstackEnqueue;
import com.disk91.forwarder.service.itf.HotspotPosition;
import com.disk91.forwarder.service.itf.sub.QueueItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ingeniousthings.tools.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class PayloadService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected boolean uplinkOpen = true;
    protected boolean downlinkOpen = true;
    boolean asyncUplinkEnable = true;
    boolean asyncDownlinkEnable = true;

    public void closeService() {
        // stop reception
        log.info("Closing Uplink");
        this.uplinkOpen = false;
        Tools.sleep(500);
        // stop Thread once queues are empty
        this.asyncUplinkEnable = false;
        log.info("Waiting for downlink expiration");
        for ( int i = 0 ; i < DOWNLINK_EXPIRATION ; i += 1000 ) {
            Tools.sleep(1000);
            log.info("Progress : "+Math.floor((100*i)/DOWNLINK_EXPIRATION)+"%");
        }
        log.info("Closing Downlink");
        this.downlinkOpen = false;
        Tools.sleep(500);
        this.asyncDownlinkEnable = false;

        boolean terminated = false;
        while ( !terminated ) {
            terminated = true;
            for (int t = 0; t < forwarderConfig.getHeliumAsyncProcessor(); t++) {
                if (uplinkThreads[t].getState() != Thread.State.TERMINATED) terminated = false;
                if (downlinkThreads[t].getState() != Thread.State.TERMINATED) terminated = false;
            }
            Tools.sleep(100);
        }
        log.info("Payload Service closed");
    }


    @Autowired
    protected PrometeusService prometeusService;

    @Autowired
    protected LocationService locationService;

    @Autowired
    protected ForwarderConfig forwarderConfig;

    Boolean threadRunningUplink[];
    Thread uplinkThreads[];

    protected ConcurrentLinkedQueue<DelayedUplink> asyncUplink = new ConcurrentLinkedQueue<>();


    @PostConstruct
    private void onStart() {
        log.info("Starting PayloadService");
        threadRunningUplink = new Boolean[forwarderConfig.getHeliumAsyncProcessor()];
        uplinkThreads = new Thread[forwarderConfig.getHeliumAsyncProcessor()];
        for ( int q = 0 ; q < forwarderConfig.getHeliumAsyncProcessor() ; q++) {
            log.debug("Prepare Thread "+q);
            threadRunningUplink[q] = Boolean.FALSE;
            Runnable r = new ProcessUplink(q, asyncUplink, threadRunningUplink[q]);
            uplinkThreads[q] = new Thread(r);
            uplinkThreads[q].start();
        }
    }

    protected enum INTEGRATION_TYPE { UNKNOWN, HTTP, MQTT }

    protected enum INTEGRATION_VERB { UNKNOWN, GET, POST, PUT }


    private class DelayedUplink {

        public INTEGRATION_TYPE type = INTEGRATION_TYPE.UNKNOWN;
        public INTEGRATION_VERB verb = INTEGRATION_VERB.UNKNOWN;
        public String endpoint;
        public String topic;
        public String urlparam;
        public KeyValue headers= new KeyValue();
        public ChipstackPayload chirpstack;
        public HeliumPayload helium = null;

        public int retry = 0;
        public long lastTrial=0;
        public long lastRecheck = 0;

    }


    public boolean asyncProcessUplink(HttpServletRequest req, ChipstackPayload c) {

        if (!this.uplinkOpen) return false;

        DelayedUplink dc = new DelayedUplink();
        dc.chirpstack = c;
        /* --- list headers
        Enumeration<String> ss = req.getHeaderNames();
        while (ss.hasMoreElements()) {
            String s = ss.nextElement();
            log.debug("Header :"+s+ " v: "+req.getHeader(s));
        }
        */

        String type = req.getHeader("htype");
        if ( type == null ) return false; // not a valid payload
        if (
                type.compareToIgnoreCase("http") == 0
            ||  type.compareToIgnoreCase("tago") == 0
        ) {
            log.debug("Got a Http Integration");
            // basically HTTP integration
            dc.type = INTEGRATION_TYPE.HTTP;
            String v = req.getHeader("hverb");
            if ( v.compareToIgnoreCase("post") == 0 ) dc.verb = INTEGRATION_VERB.POST;
            else if ( v.compareToIgnoreCase("get") == 0 ) dc.verb = INTEGRATION_VERB.GET;
            else if ( v.compareToIgnoreCase("put") == 0 ) dc.verb = INTEGRATION_VERB.PUT;
            dc.endpoint = req.getHeader("hendpoint");
            dc.urlparam = req.getHeader("hurlparam");
            String headers = req.getHeader("hheaders");
            try {
                ObjectMapper mapper = new ObjectMapper();
                dc.headers = mapper.readValue(headers, KeyValue.class);
            } catch (JsonProcessingException e) {
                log.error("Error in parsing Headers for "+c.getDeviceInfo().getDevEui());
                dc.headers = new KeyValue();
            }
            // check
            if ( dc.endpoint.length() < 5 ) return false;
            if ( dc.verb == INTEGRATION_VERB.UNKNOWN ) return false;
            if ( ! dc.endpoint.toLowerCase().startsWith("http") ) return false;
            if ( dc.endpoint.contains("internal/3.0") ) return false;
        }
        if ( type.compareToIgnoreCase("mqtt") == 0 ) {
            dc.type = INTEGRATION_TYPE.MQTT;
            dc.topic = req.getHeader("huptopic");
            dc.endpoint = req.getHeader("hendpoint");
            if ( dc.endpoint.length() < 5 ) return false;
            if ( ! dc.endpoint.startsWith("mqtt") ) return false;
            if ( dc.topic.length() < 2 ) return false;
        }
        // type.compareToIgnoreCase("google_sheets") == 0
        log.debug("Add Frame in queue");
        asyncUplink.add(dc);
        prometeusService.addUplinkInQueue();
        return true;
    }


    public class ProcessUplink implements Runnable {

        Boolean status;
        int id;
        ConcurrentLinkedQueue<DelayedUplink> queue;

        public ProcessUplink(int _id, ConcurrentLinkedQueue<DelayedUplink> _queue, Boolean _status) {
            id = _id;
            queue = _queue;
            status = _status;
        }
        public void run() {
            this.status = true;
            log.debug("Starting Payload process thread "+id);
            DelayedUplink w;
            while ( (w = queue.poll()) != null || asyncUplinkEnable ) {
                if ( w != null) {
                    // retrial limited to 3 attempt and every 10 seconds
                    long now = Now.NowUtcMs();
                    if ( w.retry > 0 && ((now - w.lastTrial) < 10_000 ) ) {
                        if ( (now - w.lastRecheck) < 500 ) {
                            // recheck too fast
                            try {
                                Thread.sleep(100);
                            } catch ( InterruptedException x) {}
                        }
                        w.lastRecheck = now;
                        queue.add(w);
                    } else {
                        log.debug("Find one in uplink queue");
                        prometeusService.remUplinkInQueue();
                        w.helium = getHeliumPayload(w.chirpstack);

                        // trace
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            log.debug(">> "+mapper.writeValueAsString(w.helium));
                        } catch (JsonProcessingException e) {
                            log.error(e.getMessage());
                            e.printStackTrace();
                        }

                        // apply integration
                        if (w.type == INTEGRATION_TYPE.HTTP) {
                            if (!processHttp(w)) {
                                w.retry++;
                                if (w.retry < 3) {
                                    prometeusService.addUplinkRetry();
                                    prometeusService.addUplinkInQueue();
                                    w.lastTrial = Now.NowUtcMs();
                                    w.lastRecheck = w.lastTrial;
                                    queue.add(w);
                                } else {
                                    prometeusService.addUplinkFailure();
                                }
                            }
                        }
                    }
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException x) {x.printStackTrace();}
                }
            }
            log.debug("Closing Payload process thread "+id);
        }
    }



    public HeliumPayload getHeliumPayload(ChipstackPayload c) {
        HeliumPayload h = new HeliumPayload();

        h.setApp_eui("0000000000000000");
        if ( c.getObject() != null ) {
            String appEui = c.getObject().getOneKey("appeui");
            if ( appEui!=null && appEui.length() == 16 ) {
                h.setApp_eui(appEui);
            }
        }

        HntDcBalance b = new HntDcBalance();
        b.setBalance(0);
        b.setNonce(0);
        h.setDc(b);

        HntDecoded hd = new HntDecoded();
        if ( c.getObject() != null && c.getObject().size() > 0 ) {
            hd.setStatus("success");
            hd.setPayload(c.getObject());
        } else {
            hd.setStatus("empty");
            hd.setPayload(new KeyValue());
        }
        h.setDecoded(hd);

        h.setDev_eui(c.getDeviceInfo().getDevEui());
        h.setDevaddr(c.getDevAddr());

        String key =  createDownlinkSession(DOWN_SESSION_TYPE.HTTP,c);
        String endpoint = forwarderConfig.getHeliumDownlinkEndpoint();
        endpoint = endpoint.replace("{key}", key);
        h.setDownlink_url(endpoint);

        h.setFcnt(c.getfCnt());
        h.setId(c.getDeduplicationId());
        h.setName(c.getDeviceInfo().getDeviceName());
        h.setPayload(c.getData());
        h.setPort(c.getfPort());
        h.setReported_at(DateConverters.StringDateToMs(c.getTime()));

        ArrayList<HntHotspot> hs = new ArrayList<>();
        for (ChirpstackRxInfo rx : c.getRxInfo()) {
          HntHotspot hh = new HntHotspot();
          hh.setId(rx.getMetadata().getGateway_id());
          hh.setName(rx.getMetadata().getGateway_name());
          HotspotPosition p = locationService.getHotspotPosition(hh.getId());
          hh.setLat(p.getPosition().getLat());
          hh.setLng(p.getPosition().getLng());
          hh.setChannel(0);
          hh.setReported_at(DateConverters.StringDateToMs(rx.getTime()));
          hh.setRssi(rx.getRssi());
          hh.setSnr(rx.getSnr());
          hh.setSpreading("NA");
          hh.setStatus("success");
          hh.setFrequency(c.getTxInfo().getFrequency()/1_000_000.0);
          hs.add(hh);
        }
        h.setHotspots(hs);

        ArrayList<HntLabel> labels = new ArrayList<>();
        HntLabel label = new HntLabel();
        label.setId(c.getDeviceInfo().getApplicationId());
        label.setName(c.getDeviceInfo().getApplicationName());
        label.setOrganization_id(c.getDeviceInfo().getTenantId());
        labels.add(label);

        HntMetadata meta = new HntMetadata();
        meta.setLabels(labels);
        meta.setOrganization_id(c.getDeviceInfo().getTenantId());
        h.setMetadata(meta);

        return h;
    }

    // ----------------------------------
    // Process HTTP
    // ----------------------------------
    protected boolean processHttp(
            DelayedUplink o
    ) {

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.USER_AGENT,"disk91_forwarder/1.0");
            for ( String k : o.headers.getEntry().keySet() ) {
                headers.add(k,o.headers.getOneKey(k));
            }
            HttpEntity<HeliumPayload> he = new HttpEntity<HeliumPayload>(o.helium,headers);
            String url=o.endpoint;
            HttpMethod m;
            switch (o.verb) {
                default:
                //case GET: m = HttpMethod.GET; break;
                case POST: m = HttpMethod.POST; break;
                case PUT: m = HttpMethod.PUT; break;
            }

            log.debug("Do "+m.name()+" to "+url);
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                            url,
                            m,
                            he,
                            String.class
                    );
            if ( responseEntity.getStatusCode().is2xxSuccessful() ) {
                return true;
            }
            return false;

        } catch (HttpClientErrorException e) {
            return false;
        } catch (HttpServerErrorException e) {
            return false;
        } catch (Exception x ) {
            return false;
        }
    }

    // ==========================================================
    // DONWLINK Session
    // ==========================================================

    protected static final long DOWNLINK_EXPIRATION = (2*Now.ONE_MINUTE);

    protected enum DOWN_SESSION_TYPE { HTTP, MQTT };
    protected class DownlinkSession implements ClonnableObject<DownlinkSession> {
        // 32 char random key for a unique sessionkey
        public String key;

        // Corresponding device Eui
        public String devEui;

        // Creation time
        public long creationTime;

        // Session type
        public DOWN_SESSION_TYPE type;

        // ---

        public DownlinkSession clone() {
            DownlinkSession d = new DownlinkSession();
            d.key = key;
            d.devEui = devEui;
            d.creationTime = creationTime;
            d.type = type;
            return d;
        }
    }
    private ObjectCache<String, DownlinkSession> downlinkCache;
    @PostConstruct
    private void initDownlinkSessionCache() {
        log.debug("initDownlinkSessionCache initialization");
        this.downlinkCache = new ObjectCache<String, DownlinkSession>("DownlinkCache", 100000, DOWNLINK_EXPIRATION) {
            @Override
            public void onCacheRemoval(String key, DownlinkSession obj, boolean batch, boolean last) {
                // nothing to do, readOnly
            }

            @Override
            public void bulkCacheUpdate(List<DownlinkSession> objects) {

            }
        };
    }

    protected String createDownlinkSession(DOWN_SESSION_TYPE t, ChipstackPayload c) {
        DownlinkSession d = new DownlinkSession();
        d.type = t;
        d.creationTime = Now.NowUtcMs();
        d.key = RandomString.getRandomAz9String(32);
        d.devEui = c.getDeviceInfo().getDevEui();
        downlinkCache.put(d,d.key);
        return d.key;
    }

    // ==========================================================
    // DONWLINK MANAGEMENT
    // ==========================================================


    protected enum DOWNLINK_TYPE { UNKNOWN, NORMAL, CLEAR };

    private class DelayedDownlink {
        public DOWNLINK_TYPE type = DOWNLINK_TYPE.UNKNOWN;
        public String devEui;
        public String payloadB64;

        public boolean confirmed;
        public int port;
        public int retry = 0;
        public long lastTrial=0;
        public long lastRecheck = 0;
    }

    Boolean downlinkThreadRunning[];
    Thread downlinkThreads[];

    protected ConcurrentLinkedQueue<DelayedDownlink> asyncDownlink[];

    @PostConstruct
    private void onStartDownlink() {
        log.info("Starting DownlinkService");
        asyncDownlink = new ConcurrentLinkedQueue[forwarderConfig.getHeliumAsyncProcessor()];
        downlinkThreadRunning = new Boolean[forwarderConfig.getHeliumAsyncProcessor()];
        downlinkThreads = new Thread[forwarderConfig.getHeliumAsyncProcessor()];
        for ( int q = 0 ; q < forwarderConfig.getHeliumAsyncProcessor() ; q++) {
            log.debug("Prepare Downlink Thread "+q);
            downlinkThreadRunning[q] = Boolean.FALSE;
            asyncDownlink[q] = new ConcurrentLinkedQueue<DelayedDownlink>();
            Runnable r = new ProcessDownlink(q,asyncDownlink[q],downlinkThreadRunning[q]);
            downlinkThreads[q] = new Thread(r);
            downlinkThreads[q].start();
        }
    }

    public synchronized boolean asyncProcessDownlink(HttpServletRequest req, String key, HeliumDownlink d) {
        if (!this.downlinkOpen) return false;

        // search for the downlink session
        DownlinkSession ds = downlinkCache.get(key);
        if ( ds == null ) return false;
        if ( ds.type != DOWN_SESSION_TYPE.HTTP ) return false;
        if ( (Now.NowUtcMs() - ds.creationTime ) > DOWNLINK_EXPIRATION) return false;

        prometeusService.updateDownlinkCacheSize(downlinkCache.cacheSize());

        DelayedDownlink dl = new DelayedDownlink();
        dl.devEui = ds.devEui;
        String s = new String(Base64.decodeBase64(d.getPayload_raw()));
        if ( s.compareToIgnoreCase("__clear_downlink_queue__") == 0 ) {
            dl.type = DOWNLINK_TYPE.CLEAR;
            dl.payloadB64 = null;
            dl.port = 0;
        } else {
            dl.type = DOWNLINK_TYPE.NORMAL;
            dl.payloadB64 = d.getPayload_raw(); // Base 64
            dl.port = d.getPort();
            dl.confirmed = d.isConfirmed();
        }
        dl.lastTrial = 0;
        dl.retry = 0;
        dl.lastRecheck = 0;

        log.debug("Add Downlink in queue");
        prometeusService.addUplinkInQueue();

        // store the same deveui in the same queue
        int q = Tools.EuiStringToByteArray(dl.devEui)[5];
        if ( q < 0 ) q += 256;
        asyncDownlink[ q % forwarderConfig.getHeliumAsyncProcessor() ].add(dl);
        return true;
    }

    public class ProcessDownlink implements Runnable {

        Boolean status;
        int id;
        ConcurrentLinkedQueue<DelayedDownlink> queue;

        public ProcessDownlink(int _id, ConcurrentLinkedQueue<DelayedDownlink> _queue, Boolean _status) {
            id = _id;
            queue = _queue;
            status = _status;
        }
        public void run() {
            this.status = true;
            log.debug("Starting Downlink process thread "+id);
            DelayedDownlink w;
            while ( (w = queue.poll()) != null || asyncDownlinkEnable ) {
                if ( w != null) {
                    // retrial limited to 3 attempt and every 10 seconds
                    long now = Now.NowUtcMs();
                    if ( w.retry > 0 && ((now - w.lastTrial) < 10_000 ) ) {
                        if ( (now - w.lastRecheck) < 500 ) {
                            // recheck too fast
                            Tools.sleep(100);
                        }
                        w.lastRecheck = now;
                        queue.add(w);
                    } else {
                        log.debug("Find one in downlink queue");
                        prometeusService.remDownlinkInQueue();
                        // apply downlink
                        if (w.type == DOWNLINK_TYPE.NORMAL) {
                            if ( ! processDownlink(w) ) {
                                w.retry++;
                                if (w.retry < 3) {
                                    prometeusService.addDownlinkInQueue();
                                    prometeusService.addDownlinkRetry();
                                    w.lastTrial = Now.NowUtcMs();
                                    w.lastRecheck = w.lastTrial;
                                    queue.add(w);
                                } else {
                                    prometeusService.addDownlinkFailure();
                                }
                            }
                        } else if ( w.type == DOWNLINK_TYPE.CLEAR ) {
                            prometeusService.remDownlinkInQueue();
                            if ( ! processDownlinkClear(w) ) {
                                w.retry++;
                                if (w.retry < 3) {
                                    prometeusService.addDownlinkInQueue();
                                    prometeusService.addDownlinkRetry();
                                    w.lastTrial = Now.NowUtcMs();
                                    w.lastRecheck = w.lastTrial;
                                    queue.add(w);
                                } else {
                                    prometeusService.addDownlinkFailure();
                                }
                            }
                        }
                    }
                } else {
                    Tools.sleep(10);
                }
            }
            log.debug("Closing Downlink process thread "+id);
        }
    }


    // ----------------------------------
    // Process Downlink
    // ----------------------------------
    protected boolean processDownlink(
            DelayedDownlink o
    ) {
        // create downlink structure
        ChirpstackEnqueue e = new ChirpstackEnqueue();
        e.setQueueItem(new QueueItem());
        e.getQueueItem().setConfirmed(o.confirmed);
        e.getQueueItem().setfPort(o.port);
        e.getQueueItem().setData(o.payloadB64);

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.USER_AGENT,"disk91_forwarder/1.0");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+forwarderConfig.getChirpstackApiAdminKey());
            HttpEntity<ChirpstackEnqueue> he = new HttpEntity<ChirpstackEnqueue>(e,headers);
            String url=forwarderConfig.getChirpstackApiBase()+"/api/devices/"+o.devEui+"/queue";
            log.debug("Do down "+o.devEui+" with "+Stuff.bytesToHex(Base64.decodeBase64(o.payloadB64))+" ( "+e.getQueueItem().getData() + " ) on port "+e.getQueueItem().getfPort());
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            he,
                            String.class
                    );
            if ( responseEntity.getStatusCode().is2xxSuccessful() ) {
                return true;
            }
            return false;
        } catch (HttpClientErrorException x) {
            log.debug("Dwn - Client error - "+x.getMessage());
            return false;
        } catch (HttpServerErrorException x) {
            log.debug("Dwn - Server error - "+x.getMessage());
            return false;
        } catch (Exception x ) {
            log.debug("Dwn - error - "+x.getMessage());
            return false;
        }
    }

    protected boolean processDownlinkClear(
            DelayedDownlink o
    ) {

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.USER_AGENT,"disk91_forwarder/1.0");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+forwarderConfig.getChirpstackApiAdminKey());
            HttpEntity he = new HttpEntity(headers);
            String url=forwarderConfig.getChirpstackApiBase()+"/api/devices/"+o.devEui+"/queue";
            log.debug("Do down "+o.devEui+" clear ");
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.DELETE,
                            he,
                            String.class
                    );
            if ( responseEntity.getStatusCode().is2xxSuccessful() ) {
                return true;
            }
            return false;
        } catch (HttpClientErrorException e) {
            log.debug("Dwn Clear - Client error - "+e.getMessage());
            return false;
        } catch (HttpServerErrorException e) {
            log.debug("Dwn Clear - Server error - "+e.getMessage());
            return false;
        } catch (Exception x ) {
            log.debug("Dwn Clear - error - "+x.getMessage());
            return false;
        }
    }


}
