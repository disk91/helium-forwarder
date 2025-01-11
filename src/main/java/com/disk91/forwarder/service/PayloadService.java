package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.api.interfaces.ChirpstackPayload;
import com.disk91.forwarder.api.interfaces.HeliumLocPayload;
import com.disk91.forwarder.api.interfaces.HeliumPayload;
import com.disk91.forwarder.api.interfaces.sub.*;
import com.disk91.forwarder.mqtt.MqttManager;
import com.disk91.forwarder.mqtt.MqttSender;
import com.disk91.forwarder.mqtt.api.FrameForwardReport;
import com.disk91.forwarder.mqtt.api.FrameForwardReportType;
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

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
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
            log.info("Progress : {}%", Math.floor((100 * i) / DownlinkService.DOWNLINK_EXPIRATION));
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

    Boolean[] threadRunningUplink;
    Thread[] uplinkThreads;

    protected ConcurrentLinkedQueue<DelayedUplink> asyncUplink = new ConcurrentLinkedQueue<>();


    @PostConstruct
    private void onStart() {
        if ( forwarderConfig.isForwarderBalancerMode() ) return;
        log.info("Starting PayloadService");

        threadRunningUplink = new Boolean[forwarderConfig.getHeliumAsyncProcessor()];
        uplinkThreads = new Thread[forwarderConfig.getHeliumAsyncProcessor()];
        for ( int q = 0 ; q < forwarderConfig.getHeliumAsyncProcessor() ; q++) {
            log.debug("Prepare Thread {}", q);
            threadRunningUplink[q] = Boolean.FALSE;
            Runnable r = new ProcessUplink(q, asyncUplink, threadRunningUplink[q]);
            uplinkThreads[q] = new Thread(r);
            uplinkThreads[q].start();
        }
    }

    protected enum INTEGRATION_TYPE { UNKNOWN, HTTP, MQTT }

    protected enum INTEGRATION_VERB { UNKNOWN, GET, POST, PUT }


    private class DelayedUplink {

        public static final int EVENT_TYPE_UPLINK = 0;
        public static final int EVENT_TYPE_LOCATION = 1;
        public static final int EVENT_TYPE_JACK = 2;
        public static final int EVET_TYPE_JOIN = 3;

        public INTEGRATION_TYPE type = INTEGRATION_TYPE.UNKNOWN;
        public INTEGRATION_VERB verb = INTEGRATION_VERB.UNKNOWN;
        public String endpoint;
        public String locendpoint;
        public String ackendpoint;
        public String joinendpoint;
        public String topicUp;
        public String topicLoc;
        public String topicAck;
        public String topicJoin;
        public String format;
        public int qos;
        public String topicDown;
        public String urlparam;
        public KeyValue headers= new KeyValue();
        public ChirpstackPayload chirpstack;
        public int eventType;
        public HeliumPayload helium = null;
        public HeliumLocPayload locPayload = null;

        public int retry = 0;
        public long lastTrial=0;
        public long lastRecheck = 0;

    }

    // Make sure a topic contains only a-Z A-Z 0-9 -_{} . and / characters
    public boolean isTopicFormatAcceptable(String topic) {
        boolean r = topic.matches("^[a-zA-Z0-9_{}./\\-]+$");
        if ( !r ) log.warn("Got an invalid topic ({})", topic);
        return r;
    }

    public boolean asyncProcessEvent(HttpServletRequest req, ChirpstackPayload c, String evtType) {
        if ( forwarderConfig.isForwarderBalancerMode() ) return false;
        if (!this.uplinkOpen) return false;

        DelayedUplink dc = new DelayedUplink();
        dc.chirpstack = c;
        if ( evtType.compareToIgnoreCase("up") == 0 ) {
            dc.eventType = DelayedUplink.EVENT_TYPE_UPLINK;
        } else if ( evtType.compareToIgnoreCase("location") == 0 ) {
            dc.eventType = DelayedUplink.EVENT_TYPE_LOCATION;
        } else if ( evtType.compareToIgnoreCase("ack") == 0 ) {
            dc.eventType = DelayedUplink.EVENT_TYPE_JACK;
        } else if ( evtType.compareToIgnoreCase("join") == 0 ) {
            dc.eventType = DelayedUplink.EVET_TYPE_JOIN;
        } else {
            log.error("Invalid Type received ({})", evtType);
            return false;
        }

        /* --- list headers
        Enumeration<String> ss = req.getHeaderNames();
        while (ss.hasMoreElements()) {
            String s = ss.nextElement();
            log.debug("Header :"+s+ " v: "+req.getHeader(s));
        }
        */

        String type = req.getHeader("htype");
        if ( type == null ) return false; // not a valid payload
        type = type.trim(); // some user do it ...
        if (
                type.compareToIgnoreCase("http") == 0
            ||  type.compareToIgnoreCase("tago") == 0
        ) {
            log.debug("Got a Http Integration");
            // basically HTTP integration
            dc.type = INTEGRATION_TYPE.HTTP;
            String v = req.getHeader("hverb");
            if ( v!=null ) v = v.trim();
            else v="unknown";   // default behavior
            if ( v.compareToIgnoreCase("post") == 0 ) dc.verb = INTEGRATION_VERB.POST;
            else if ( v.compareToIgnoreCase("get") == 0 ) dc.verb = INTEGRATION_VERB.GET;
            else if ( v.compareToIgnoreCase("put") == 0 ) dc.verb = INTEGRATION_VERB.PUT;
            dc.endpoint = req.getHeader("hendpoint");
            dc.locendpoint = req.getHeader("hlocendpoint");
            dc.ackendpoint = req.getHeader("hackendpoint");
            dc.urlparam = req.getHeader("hurlparam");

            dc.format = req.getHeader("hformat");
            if (dc.format != null ) {
                dc.format = dc.format.trim();
                if (dc.format.compareTo("chipstack") != 0) dc.format = "helium";
                else dc.format = "chipstack";
            } else dc.format = "helium";

            if ( dc.endpoint != null ) dc.endpoint = dc.endpoint.trim(); else return false;
            if ( dc.locendpoint != null ) dc.locendpoint = dc.locendpoint.trim();
            if (dc.ackendpoint != null ) dc.ackendpoint = dc.ackendpoint.trim();
            if ( dc.urlparam != null ) dc.urlparam = dc.urlparam.trim();
            String headers = req.getHeader("hheaders");
            if ( headers != null ) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    dc.headers = mapper.readValue(headers, KeyValue.class);
                } catch (JsonProcessingException e) {
                    log.error("Error in parsing Headers for {}", c.getDeviceInfo().getDevEui());
                    dc.headers = new KeyValue();
                }
            } else dc.headers = new KeyValue();

            // check
            if ( dc.endpoint.length() < 5 ) return false;
            if ( ! dc.endpoint.toLowerCase().startsWith("http") ) return false;
            if ( dc.endpoint.contains("internal/3.0") ) return false;

            if ( dc.locendpoint == null || dc.locendpoint.length() < 5 ) {
                dc.locendpoint = null;
            } else {
                if ( ! dc.locendpoint.toLowerCase().startsWith("http") ) return false;
                if ( dc.locendpoint.contains("internal/3.0") ) return false;
            }

            if ( dc.ackendpoint == null || dc.ackendpoint.length() < 5 ) dc.ackendpoint = null;
            else {
                if (!dc.ackendpoint.toLowerCase().startsWith("http")) return false;
                if (dc.ackendpoint.contains("internal/3.0")) return false;
            }

            if ( dc.joinendpoint == null || dc.joinendpoint.length() < 5 ) dc.joinendpoint = null;
            else {
                if (!dc.joinendpoint.toLowerCase().startsWith("http")) return false;
                if (dc.joinendpoint.contains("internal/3.0")) return false;
            }

            if ( dc.verb == INTEGRATION_VERB.UNKNOWN ) return false;

        } else if ( type.compareToIgnoreCase("mqtt") == 0 ) {
            log.debug("Got a MQTT Integration");
            dc.type = INTEGRATION_TYPE.MQTT;
            dc.topicUp = req.getHeader("huptopic");
            if ( dc.topicUp != null ) dc.topicUp = dc.topicUp.trim(); else return false;
            if ( !isTopicFormatAcceptable(dc.topicUp) ) return false;

            if ( req.getHeader("hloctopic") != null ) {
                dc.topicLoc = req.getHeader("hloctopic");
            } else {
                dc.topicLoc = dc.topicUp+"/location";
            }
            if ( !isTopicFormatAcceptable(dc.topicLoc) ) return false;

            dc.topicDown = req.getHeader("hdntopic");
            if ( dc.topicDown != null ) {
                dc.topicDown = dc.topicDown.trim();
                if ( !isTopicFormatAcceptable(dc.topicDown) ) return false;
            } else dc.topicDown = "";

            dc.topicAck = req.getHeader("hacktopic");
            if (dc.topicAck != null ) {
                dc.topicAck = dc.topicAck.trim();
                if ( !isTopicFormatAcceptable(dc.topicAck) ) return false;
            } else dc.topicAck = "";

            dc.topicJoin = req.getHeader("hjointopic");
            if (dc.topicJoin != null ) {
                dc.topicJoin = dc.topicJoin.trim();
                if ( !isTopicFormatAcceptable(dc.topicJoin) ) return false;
            }
            else dc.topicJoin = "";

            dc.format = req.getHeader("hformat");
            if (dc.format != null ) {
                dc.format = dc.format.trim();
                if (dc.format.compareTo("chipstack") != 0) dc.format = "helium";
                else dc.format = "chipstack";
            } else dc.format = "helium";

            String sQos = req.getHeader("hqos");
            if ( sQos != null ) sQos = sQos.trim();
            dc.qos = -1;
            if ( sQos != null && sQos.length() == 1 ) {
                try {
                    dc.qos = Integer.parseInt(sQos);
                } catch (Exception e) {
                    dc.qos = -1;
                };
            }
            dc.endpoint = req.getHeader("hendpoint");
            if ( dc.endpoint != null ) dc.endpoint = dc.endpoint.trim(); else return false;
            if ( dc.endpoint.length() < 5 ) return false;
            if ( ! dc.endpoint.startsWith("mqtt") ) return false;
            if ( dc.topicUp.length() < 3 ) return false;
            if ( ! dc.topicDown.isEmpty() && dc.topicDown.length() < 3 ) return false;
        } else {
            // unsupported type
            log.warn("Received unsupported type ({})", type.substring(0, Math.min(type.length(), 6)));
        }
        // type.compareToIgnoreCase("google_sheets") == 0
        log.debug("Add Frame in queue ({})", type);
        asyncUplink.add(dc);
        prometeusService.addUplinkInQueue();
        return true;
    }

    @Autowired
    protected MqttSender mqttSender;

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
            log.debug("Starting Payload process thread {}", id);
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
                            log.debug("Find one in message in queue");
                            prometeusService.remUplinkInQueue();

                            String message = "";
                            if ( w.eventType == DelayedUplink.EVENT_TYPE_UPLINK ) {
                                w.helium = getHeliumPayload(w.chirpstack);
                                // trace
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    if (w.format != null && w.format.compareTo("chipstack") == 0) {
                                        message = mapper.writeValueAsString(w.chirpstack);
                                    } else {
                                        message = mapper.writeValueAsString(w.helium);
                                    }
                                    log.debug("UP : {}", message);
                                } catch (JsonProcessingException e) {
                                    log.error(e.getMessage());
                                }
                            } else if (w.eventType == DelayedUplink.EVENT_TYPE_LOCATION ) {
                                w.locPayload = getHeliumLocPayload(w.chirpstack);
                                // trace
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    message = mapper.writeValueAsString(w.locPayload);
                                    log.debug("LOC : {}", message);
                                } catch (JsonProcessingException e) {
                                    log.error(e.getMessage());
                                    e.printStackTrace();
                                }
                            } else if (w.eventType == DelayedUplink.EVENT_TYPE_JACK) {
                                w.helium = getHeliumJoinAckPayload(w.chirpstack);
                                // trace
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    message = mapper.writeValueAsString(w.chirpstack);
                                    log.debug("JACK : {}", message);
                                } catch (JsonProcessingException e) {
                                    log.error(e.getMessage());
                                    e.printStackTrace();
                                }
                            } else if (w.eventType == DelayedUplink.EVET_TYPE_JOIN) {
                                w.helium = getHeliumJoinAckPayload(w.chirpstack);
                                // trace
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    message = mapper.writeValueAsString(w.chirpstack);
                                    log.debug("JOIN : {}", message);
                                } catch (JsonProcessingException e) {
                                    log.error(e.getMessage());
                                    e.printStackTrace();
                                }
                            } else {
                                log.error("Invalid type of event ({})", w.eventType);
                                continue;
                            }

                            // apply integration
                            if (w.type == INTEGRATION_TYPE.HTTP) {
                                int returnCode = processHttp(w);
                                if (returnCode != 200) {
                                    mqttSender.publishMessage(
                                            "helium/forwarder/process/",
                                            new FrameForwardReport(
                                                    w.helium.getDev_eui(),
                                                    w.chirpstack.getDeduplicationId(),
                                                    w.retry > 0 ? FrameForwardReportType.HTTP_RETRY_FAILURE : FrameForwardReportType.HTTP_FAILURE,
                                                    ""+returnCode,
                                                    message
                                            ),0
                                    );
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
                                } else {
                                    mqttSender.publishMessage(
                                            "helium/forwarder/process/",
                                            new FrameForwardReport(
                                                    w.helium.getDev_eui(),
                                                    w.chirpstack.getDeduplicationId(),
                                                    w.retry > 0 ? FrameForwardReportType.HTTP_RETRY_SUCCESS : FrameForwardReportType.HTTP_SUCCESS,
                                                    ""+returnCode,
                                                    message
                                            ),0
                                    );
                                }
                            } else if ( w.type == INTEGRATION_TYPE.MQTT ) {
                                if (!processMqtt(w)) {
                                    mqttSender.publishMessage(
                                            "helium/forwarder/process/",
                                            new FrameForwardReport(
                                                    w.helium.getDev_eui(),
                                                    w.chirpstack.getDeduplicationId(),
                                                    w.retry > 0 ? FrameForwardReportType.MQTT_RETRY_FAILURE : FrameForwardReportType.MQTT_FAILURE,
                                                    "FAILED",
                                                    message
                                            ),0
                                    );
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
                                } else {
                                    mqttSender.publishMessage(
                                            "helium/forwarder/process/",
                                            new FrameForwardReport(
                                                    w.helium.getDev_eui(),
                                                    w.chirpstack.getDeduplicationId(),
                                                    w.retry > 0 ? FrameForwardReportType.MQTT_RETRY_SUCCESS : FrameForwardReportType.MQTT_SUCCESS,
                                                    "SUCCESS",
                                                    message
                                            ),0
                                    );
                                }
                            } else {
                                log.warn("Received an invalid type {}", w.type);
                            }
                        }
                    } else {
                        Tools.sleep(10);
                    }
                } catch (Exception x) {
                    log.error("Exception in processing frame {}", x.getMessage());
                    x.printStackTrace();
                } finally {
                    w = queue.poll();
                }
            }
            log.debug("Closing Payload process thread {}", id);
        }
    }


    public HeliumLocPayload getHeliumLocPayload(ChirpstackPayload c) {

        // get app_eui
        String appEui = "0000000000000000";
        if ( c.getObject() != null ) {
            String _appEui = c.getObject().getOneKey("appeui");
            if ( _appEui!=null && _appEui.length() == 16 ) {
                appEui = _appEui;
            }
        }

        HeliumLocPayload payload = new HeliumLocPayload();
        payload.setDev_eui(c.getDeviceInfo().getDevEui());
        payload.setApp_eui(appEui);
        payload.setName(c.getDeviceInfo().getDeviceName());
        payload.setOrganization_id(c.getDeviceInfo().getTenantId());
        payload.setLatitude(c.getLocation().getLatitude());
        payload.setLongitude(c.getLocation().getLongitude());
        payload.setAccuracy(c.getLocation().getAccuracy());
        payload.setSource(c.getLocation().getSource());
        return payload;
    }

    public HeliumPayload getHeliumJoinAckPayload(ChirpstackPayload c) {

        // get app_eui (default as this is not in the payload)
        String appEui = "0000000000000000";

        HeliumPayload payload = new HeliumPayload();
        payload.setDev_eui(c.getDeviceInfo().getDevEui());
        payload.setApp_eui(appEui);
        payload.setName(c.getDeviceInfo().getDeviceName());
        payload.setMetadata(new HntMetadata());
        payload.getMetadata().setOrganization_id(c.getDeviceInfo().getTenantId());
        return payload;
    }


    public HeliumPayload getHeliumPayload(ChirpstackPayload c) {
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
        // this must be a device id but this is not existing with chirpstack, creating one from deveui
        // 7c523974-4ce7-4a92-948b-55171a6e4d77 deveui is 16c
        h.setId(
              c.getDeviceInfo().getDevEui().substring(0,8)+"-"
            + c.getDeviceInfo().getDevEui().substring(8,12)+"-"
            + c.getDeviceInfo().getDevEui().substring(12,16)+"-"
            + c.getDeviceInfo().getDevEui().substring(0,4)+"-"
            + c.getDeviceInfo().getDevEui().substring(4,16)
        );
        h.setUuid(c.getDeduplicationId());
        h.setType("uplink");
        h.setName(c.getDeviceInfo().getDeviceName());
        h.setPayload(c.getData());
        h.setPort(c.getfPort());
        h.setReported_at(DateConverters.StringDateToMs(c.getTime()));

        ArrayList<HntHotspot> hs = new ArrayList<>();
        for (ChirpstackRxInfo rx : c.getRxInfo()) {
          HntHotspot hh = new HntHotspot();
          hh.setId(rx.getMetadata().getGateway_id());
          hh.setName(rx.getMetadata().getGateway_name());

          // check if position is in the meta
          boolean foundLoc = false;
          if (   rx.getMetadata().getGateway_lat() != null && rx.getMetadata().getGateway_lat().length() > 1
              && rx.getMetadata().getGateway_long() != null && rx.getMetadata().getGateway_long().length() > 1
          ) {
                // we have a pos, convert string to double
                try {
                    rx.getMetadata().setLon(Double.parseDouble(rx.getMetadata().getGateway_long()));
                    rx.getMetadata().setLat(Double.parseDouble(rx.getMetadata().getGateway_lat()));
                    hh.setLat(rx.getMetadata().getLat());
                    hh.setLng(rx.getMetadata().getLon());
                    foundLoc = true;
                } catch ( NumberFormatException x ) {
                    log.warn("Invalid Gateway location 2 ("+rx.getMetadata().getGateway_lat()+", "+rx.getMetadata().getGateway_long()+")");
                }
          }
          if ( !foundLoc ) {
              HotspotPosition p = locationService.getHotspotPosition(hh.getId());
              hh.setLat(p.getPosition().getLat());
              hh.setLng(p.getPosition().getLng());
          }

          hh.setChannel(0);
          if ( rx.getGwTime() == null ) {
              // in version 4.6.0 the gw time has been renamed to gwTime
              // if this is used with a version < 4.6 the field is time
              hh.setReported_at(DateConverters.StringDateToMs(rx.getTime()));
          } else {
              hh.setReported_at(DateConverters.StringDateToMs(rx.getGwTime()));
          }
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

    public ChirpstackPayload enrichPayload(ChirpstackPayload c ) {

        if ( c.getRxInfo() != null ) {

            for ( ChirpstackRxInfo rx :c.getRxInfo() ) {
                rx.setTime(rx.getGwTime()); // for retro compatibility
                if ( rx.getMetadata() != null ) {
                    // check if position is in the meta
                    boolean foundLoc = false;
                    if ( rx.getMetadata().getGateway_lat() != null && rx.getMetadata().getGateway_lat().length() > 1
                      && rx.getMetadata().getGateway_long() != null && rx.getMetadata().getGateway_long().length() > 1
                    ) {
                        // we have a pos, convert string to double
                        try {
                            rx.getMetadata().setLon(Double.parseDouble(rx.getMetadata().getGateway_long()));
                            rx.getMetadata().setLat(Double.parseDouble(rx.getMetadata().getGateway_lat()));
                            foundLoc = true;
                        } catch ( NumberFormatException x ) {
                            log.warn("Invalid Gateway location ({}, {})", rx.getMetadata().getGateway_lat(), rx.getMetadata().getGateway_long());
                        }
                    }
                    if ( !foundLoc ) {
                        HotspotPosition p = locationService.getHotspotPosition(rx.getMetadata().getGateway_id());
                        rx.getMetadata().setLon(p.getPosition().getLng());
                        rx.getMetadata().setLat(p.getPosition().getLat());
                    }
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
            o.topicLoc,
            o.topicAck,
            o.topicDown,
            o.topicJoin,
            o.format,
            o.qos
        );
        if ( m != null ) {
            if ( o.eventType == DelayedUplink.EVENT_TYPE_UPLINK ) {
                return m.publishMessage(o.helium,o.chirpstack);
            } else if ( o.eventType == DelayedUplink.EVENT_TYPE_LOCATION ) {
                return m.publishLocation(o.locPayload,o.chirpstack);
            } else if (o.eventType == DelayedUplink.EVENT_TYPE_JACK) {
                return m.publishAck(o.helium,o.chirpstack);
            } else if (o.eventType == DelayedUplink.EVET_TYPE_JOIN) {
                return m.publishJoin(o.helium,o.chirpstack);
            }
        }
        return false;
    }


    // ----------------------------------
    // Process HTTP
    // ----------------------------------
    protected int processHttp(
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
            if ( o.eventType == DelayedUplink.EVENT_TYPE_UPLINK ) {

                HttpEntity<HeliumPayload> he = new HttpEntity<HeliumPayload>(o.helium, headers);
                String url = o.endpoint;
                HttpMethod m;
                switch (o.verb) {
                    default:
                        //case GET: m = HttpMethod.GET; break;
                    case POST:
                        m = HttpMethod.POST;
                        break;
                    case PUT:
                        m = HttpMethod.PUT;
                        break;
                }

                log.debug("Do {} to {}", m.name(), url);
                ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                        url,
                        m,
                        he,
                        String.class
                    );
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    return 200;
                }
                log.debug("Return code was {}", responseEntity.getStatusCode());
                return responseEntity.getStatusCode().value();

            } else if ( o.eventType == DelayedUplink.EVENT_TYPE_LOCATION && o.locendpoint != null ) {

                HttpEntity<HeliumLocPayload> he = new HttpEntity<HeliumLocPayload>(o.locPayload, headers);
                String url = o.locendpoint;
                HttpMethod m;
                switch (o.verb) {
                    default:
                        //case GET: m = HttpMethod.GET; break;
                    case POST:
                        m = HttpMethod.POST;
                        break;
                    case PUT:
                        m = HttpMethod.PUT;
                        break;
                }

                log.debug("Loc - Do {} to {}", m.name(), url);
                ResponseEntity<String> responseEntity =
                        restTemplate.exchange(
                                url,
                                m,
                                he,
                                String.class
                        );
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    return 200;
                }
                log.debug("Return code was {}", responseEntity.getStatusCode());
                return responseEntity.getStatusCode().value();
            } else if (o.eventType == DelayedUplink.EVENT_TYPE_JACK && o.ackendpoint != null) {
                HttpEntity<ChirpstackPayload> he = new HttpEntity<ChirpstackPayload>(o.chirpstack, headers);
                String url = o.ackendpoint;
                HttpMethod m;
                switch (o.verb) {
                    default:
                        //case GET: m = HttpMethod.GET; break;
                    case POST:
                        m = HttpMethod.POST;
                        break;
                    case PUT:
                        m = HttpMethod.PUT;
                        break;
                }

                log.debug("Ack - Do {} to {}", m.name(), url);
                ResponseEntity<String> responseEntity =
                        restTemplate.exchange(
                                url,
                                m,
                                he,
                                String.class
                        );
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    return 200;
                }
                log.debug("Return code was {}", responseEntity.getStatusCode());
                return responseEntity.getStatusCode().value();
            } else if (o.eventType == DelayedUplink.EVET_TYPE_JOIN && o.joinendpoint != null) {
                HttpEntity<ChirpstackPayload> he = new HttpEntity<ChirpstackPayload>(o.chirpstack, headers);
                String url = o.joinendpoint;
                HttpMethod m;
                switch (o.verb) {
                    default:
                        //case GET: m = HttpMethod.GET; break;
                    case POST:
                        m = HttpMethod.POST;
                        break;
                    case PUT:
                        m = HttpMethod.PUT;
                        break;
                }

                log.debug("Join - Do {} to {}", m.name(), url);
                ResponseEntity<String> responseEntity =
                        restTemplate.exchange(
                                url,
                                m,
                                he,
                                String.class
                        );
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    return 200;
                }
                log.debug("Return code was {}", responseEntity.getStatusCode());
                return responseEntity.getStatusCode().value();
            } else {
                log.error("Invalid event type ({})", o.eventType);
                return 200; // no need to retry this
            }

        } catch (HttpClientErrorException e) {
            log.debug("Http client error : {}", e.getMessage());
            return 1000;
        } catch (HttpServerErrorException e) {
            log.debug("Http server error : {}", e.getMessage());
            return 1001;
        } catch (Exception x ) {
            log.debug("Http error : {}", x.getMessage());
            return 1002;
        }
    }




}
