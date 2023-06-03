package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.api.interfaces.ChipstackPayload;
import com.disk91.forwarder.api.interfaces.HeliumPayload;
import com.disk91.forwarder.api.interfaces.sub.*;
import com.disk91.forwarder.service.itf.HotspotPosition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ingeniousthings.tools.DateConverters;
import fr.ingeniousthings.tools.ITNotFoundException;
import fr.ingeniousthings.tools.ITParseException;
import fr.ingeniousthings.tools.Now;
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
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class PayloadService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected LocationService locationService;

    @Autowired
    protected ForwarderConfig forwarderConfig;

    Boolean threadRunning[];
    Thread threads[];

    protected ConcurrentLinkedQueue<DelayedConversion> asyncConversion = new ConcurrentLinkedQueue<>();
    boolean asyncPayloadEnable = true;


    @PostConstruct
    private void onStart() {
        log.info("Starting PayloadService");
        threadRunning = new Boolean[forwarderConfig.getHeliumAsyncProcessor()];
        threads = new Thread[forwarderConfig.getHeliumAsyncProcessor()];
        for ( int q = 0 ; q < forwarderConfig.getHeliumAsyncProcessor() ; q++) {
            log.debug("Prepare Thread "+q);
            threadRunning[q] = Boolean.FALSE;
            Runnable r = new ProcessPayload(q,asyncConversion,threadRunning[q]);
            threads[q] = new Thread(r);
            threads[q].start();
        }
    }

    protected enum INTEGRATION_TYPE {
        UNKNOWN,
        HTTP,
        MQTT
    }

    protected enum INTEGRATION_VERB {
        UNKNOWN,
        GET,
        POST,
        PUT
    }


    private class DelayedConversion {

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


    public boolean asyncProcessPayload(HttpServletRequest req, ChipstackPayload c) {

        DelayedConversion dc = new DelayedConversion();
        dc.chirpstack = c;
        /* --- liste headers
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
        asyncConversion.add(dc);
        return true;
    }


    public class ProcessPayload implements Runnable {

        Boolean status;
        int id;
        ConcurrentLinkedQueue<DelayedConversion> queue;

        public ProcessPayload(int _id, ConcurrentLinkedQueue<DelayedConversion> _queue, Boolean _status) {
            id = _id;
            queue = _queue;
            status = _status;
        }
        public void run() {
            this.status = true;
            log.debug("Starting Payload process thread "+id);
            DelayedConversion w;
            while ( (w = queue.poll()) != null || asyncPayloadEnable ) {
                if ( w != null) {
                    log.debug("Find one in queue");
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
                                    w.lastTrial = Now.NowUtcMs();
                                    w.lastRecheck = w.lastTrial;
                                    queue.add(w);
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

        // todo downlink
        h.setDownlink_url("todo");

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
            DelayedConversion o
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

}
