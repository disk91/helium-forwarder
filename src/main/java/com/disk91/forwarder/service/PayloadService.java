package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.api.interfaces.ChipstackPayload;
import com.disk91.forwarder.api.interfaces.HeliumPayload;
import com.disk91.forwarder.api.interfaces.sub.*;
import com.disk91.forwarder.mqtt.MqttManager;
import com.disk91.forwarder.service.itf.HotspotPosition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ingeniousthings.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class PayloadService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected DownlinkService downlinkService;

    protected boolean uplinkOpen = true;
    protected volatile boolean asyncUplinkEnable = true;

    protected boolean closeForRequest = false;
    public boolean isStateClose() { return closeForRequest; }

    public void closeService() {
        if ( forwarderConfig.isForwarderBalancerMode() ) return;

        // stop api input
        log.info("Close Uplink request");
        this.closeForRequest = true;
        Tools.sleep(10_000);
        // stop reception
        log.info("Closing Uplink");
        this.uplinkOpen = false;
        Tools.sleep(500);
        // stop Thread once queues are empty
        this.asyncUplinkEnable = false;
        log.info("Waiting for downlink expiration");
        for ( int i = 0 ; i < DownlinkService.DOWNLINK_EXPIRATION ; i += 1000 ) {
            Tools.sleep(1000);
            log.info("Progress : "+Math.floor((100*i)/DownlinkService.DOWNLINK_EXPIRATION)+"%");
        }
        log.info("Closing Downlink");
        downlinkService.stopDownlinks();

        boolean terminated = false;
        while ( !terminated ) {
            terminated = true;
            for (int t = 0; t < forwarderConfig.getHeliumAsyncProcessor(); t++) {
                if (uplinkThreads[t].getState() != Thread.State.TERMINATED) terminated = false;
                if ( ! downlinkService.isDownlinkThreadTerminated(t) ) terminated = false;
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
        if ( forwarderConfig.isForwarderBalancerMode() ) return;
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
        public String topicUp;
        public int qos;
        public String topicDown;
        public String urlparam;
        public KeyValue headers= new KeyValue();
        public ChipstackPayload chirpstack;
        public HeliumPayload helium = null;

        public int retry = 0;
        public long lastTrial=0;
        public long lastRecheck = 0;

    }


    public boolean asyncProcessUplink(HttpServletRequest req, ChipstackPayload c) {
        if ( forwarderConfig.isForwarderBalancerMode() ) return false;
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
        } else if ( type.compareToIgnoreCase("mqtt") == 0 ) {
            log.debug("Got a MQTT Integration");
            dc.type = INTEGRATION_TYPE.MQTT;
            dc.topicUp = req.getHeader("huptopic");
            dc.topicDown = req.getHeader("hdntopic");
            String sQos = req.getHeader("hqos");
            dc.qos = -1;
            if ( sQos != null && sQos.length() == 1 ) {
                try {
                    dc.qos = Integer.parseInt(sQos);
                } catch (Exception e) {
                    dc.qos = -1;
                };
            }
            dc.endpoint = req.getHeader("hendpoint");
            if ( dc.endpoint.length() < 5 ) return false;
            if ( ! dc.endpoint.startsWith("mqtt") ) return false;
            if ( dc.topicUp.length() < 3 ) return false;
            if ( dc.topicDown.length() > 0 && dc.topicDown.length() < 3 ) return false;
        } else {
            // unsupported type
            log.warn("Received unsupported type ("+type.substring(0,Math.min(type.length(), 6))+")");
        }
        // type.compareToIgnoreCase("google_sheets") == 0
        log.debug("Add Frame in queue ("+type+")");
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
            DelayedUplink w = queue.poll();
            while ( w != null || asyncUplinkEnable ) {
                try {
                    if (w != null) {
                        // retrial limited to 3 attempt and every 10 seconds
                        long now = Now.NowUtcMs();
                        if (w.retry > 0 && ((now - w.lastTrial) < 10_000)) {
                            if ((now - w.lastRecheck) < 500) {
                                // recheck too fast
                                Tools.sleep(100);
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
                                log.debug(">> " + mapper.writeValueAsString(w.helium));
                            } catch (JsonProcessingException e) {
                                log.error(e.getMessage());
                                e.printStackTrace();
                            }

                            // apply integration
                            if (w.type == INTEGRATION_TYPE.HTTP) {
                                if (!processHttp(w)) {
                                    w.retry++;
                                    if (w.retry < 3) {
                                        log.debug("Http failure, add to retry");
                                        prometeusService.addUplinkRetry();
                                        prometeusService.addUplinkInQueue();
                                        w.lastTrial = Now.NowUtcMs();
                                        w.lastRecheck = w.lastTrial;
                                        queue.add(w);
                                    } else {
                                        prometeusService.addUplinkFailure();
                                    }
                                }
                            } else if ( w.type == INTEGRATION_TYPE.MQTT ) {
                                if (!processMqtt(w)) {
                                    w.retry++;
                                    if (w.retry < 3) {
                                        log.debug("Mqtt failure, add to retry");
                                        prometeusService.addUplinkRetry();
                                        prometeusService.addUplinkInQueue();
                                        w.lastTrial = Now.NowUtcMs();
                                        w.lastRecheck = w.lastTrial;
                                        queue.add(w);
                                    } else {
                                        prometeusService.addUplinkFailure();
                                    }
                                }
                            } else {
                                log.warn("Received an invalid type "+w.type);
                            }
                        }
                    } else {
                        Tools.sleep(10);
                    }
                } catch (Exception x) {
                    log.error("Exception in processing frame "+x.getMessage());
                    x.printStackTrace();
                } finally {
                    w = queue.poll();
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

        String key =  downlinkService.createDownlinkSession(DownlinkService.DOWN_SESSION_TYPE.HTTP,c);
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

          int bw = c.getTxInfo().getModulation().getLora().getBandwidth();
          int sf = c.getTxInfo().getModulation().getLora().getSpreadingFactor();
          hh.setSpreading("SF"+sf+"BW"+(bw/1000));
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

    public ChipstackPayload enrichPayload( ChipstackPayload c ) {

        if ( c.getRxInfo() != null ) {

            for ( ChirpstackRxInfo rx :c.getRxInfo() ) {
                if ( rx.getMetadata() != null ) {
                    HotspotPosition p = locationService.getHotspotPosition(rx.getMetadata().getGateway_id());
                    rx.getMetadata().setLon(p.getPosition().getLng());
                    rx.getMetadata().setLat(p.getPosition().getLat());
                }
            }

        }

        return c;
    }

    // ----------------------------------
    // Process MQTT
    // ----------------------------------
    @Autowired
    protected MqttConnectionService mqttConnectionService;

    protected boolean processMqtt(
        DelayedUplink o
    ) {
        MqttManager m = mqttConnectionService.getMqttManager(
            o.endpoint,
            null,
            o.topicUp,
            o.topicDown,
            o.qos
        );
        if ( m != null ) {
            return m.publishMessage(o.helium);
        }
        return false;
    }


    // ----------------------------------
    // Process HTTP
    // ----------------------------------
    protected boolean processHttp(
            DelayedUplink o
    ) {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(800);
        factory.setReadTimeout(1500);
        RestTemplate restTemplate = new RestTemplate(factory);
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
            log.debug("Return code was "+responseEntity.getStatusCode());
            return false;

        } catch (HttpClientErrorException e) {
            log.debug("Http client error : "+e.getMessage());
            return false;
        } catch (HttpServerErrorException e) {
            log.debug("Http server error : "+e.getMessage());
            return false;
        } catch (Exception x ) {
            log.debug("Http error : "+x.getMessage());
            return false;
        }
    }




}
