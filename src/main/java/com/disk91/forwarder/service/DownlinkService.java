package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.api.interfaces.ChirpstackPayload;
import com.disk91.forwarder.api.interfaces.HeliumDownlink;
import com.disk91.forwarder.api.interfaces.HeliumMqttDownlinkPayload;
import com.disk91.forwarder.service.itf.ChirpstackEnqueue;
import com.disk91.forwarder.service.itf.sub.QueueItem;
import fr.ingeniousthings.tools.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DownlinkService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ForwarderConfig forwarderConfig;

    @Autowired
    protected PrometeusService prometeusService;

    protected boolean asyncDownlinkEnable = true;

    protected boolean downlinkOpen = true;


    public void stopDownlinks() {
        this.downlinkOpen = false;
        Tools.sleep(500);
        this.asyncDownlinkEnable = false;
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
        if ( forwarderConfig.isForwarderBalancerMode() ) return;
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

    protected String createDownlinkSession(DOWN_SESSION_TYPE t, ChirpstackPayload c) {
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

    protected static class DelayedDownlink {
        public DOWNLINK_TYPE type = DOWNLINK_TYPE.UNKNOWN;
        public String devEui;
        public String payloadB64;

        public boolean confirmed;
        public int port;
        public int retry = 0;
        public long lastTrial=0;
        public long lastRecheck = 0;
    }

    Boolean[] downlinkThreadRunning;
    Thread[] downlinkThreads;

    protected ConcurrentLinkedQueue[] asyncDownlink;

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void onStartDownlink() {
        if ( forwarderConfig.isForwarderBalancerMode() ) return;
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

    public boolean isDownlinkThreadTerminated(int t) {
        return (downlinkThreads[t].getState() == Thread.State.TERMINATED);
    }

    @SuppressWarnings("unchecked")
    public synchronized boolean asyncProcessDownlink(HttpServletRequest req, String key, HeliumDownlink d) {
        if ( forwarderConfig.isForwarderBalancerMode() ) return false;
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
        if ( s.compareToIgnoreCase("__clear_downlink_queue__") == 0 || d.getPayload_raw().compareToIgnoreCase("__clear_downlink_queue__") == 0 ) {
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
        prometeusService.addDownlinkInQueue();

        // store the same deveui in the same queue
        int q = Tools.EuiStringToByteArray(dl.devEui)[5];
        if ( q < 0 ) q += 256;
        asyncDownlink[ q % forwarderConfig.getHeliumAsyncProcessor() ].add(dl);
        return true;
    }

    @SuppressWarnings("unchecked")
    public synchronized boolean asyncProcessMqttDownlink(HeliumMqttDownlinkPayload d, String deviceId) {
        if ( forwarderConfig.isForwarderBalancerMode() ) return false;
        if (!this.downlinkOpen) return false;

        prometeusService.updateDownlinkCacheSize(downlinkCache.cacheSize());

        DelayedDownlink dl = new DelayedDownlink();
        dl.devEui = deviceId;
        String s = new String(Base64.decodeBase64(d.getPayload_raw()));
        if ( s.compareToIgnoreCase("__clear_downlink_queue__") == 0 || d.getPayload_raw().compareToIgnoreCase("__clear_downlink_queue__") == 0 ) {
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
        prometeusService.addDownlinkInQueue();

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
            HttpEntity<String> he = new HttpEntity<String>(headers);
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
