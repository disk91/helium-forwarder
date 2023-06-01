package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.api.interfaces.ChipstackPayload;
import com.disk91.forwarder.api.interfaces.HeliumPayload;
import com.disk91.forwarder.api.interfaces.sub.*;
import com.disk91.forwarder.service.itf.HotspotPosition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ingeniousthings.tools.DateConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
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
        threadRunning = new Boolean[forwarderConfig.getHeliumAsyncProcessor()];
        threads = new Thread[forwarderConfig.getHeliumAsyncProcessor()];
        for ( int q = 0 ; q < forwarderConfig.getHeliumAsyncProcessor() ; q++) {
            threadRunning[q] = Boolean.FALSE;
            Runnable r = new ProcessPayload(q,asyncConversion,threadRunning[q]);
            threads[q] = new Thread(r);
            threads[q].start();
        }
    }


    private class DelayedConversion {

       // public HttpHeaders headers;
        public ChipstackPayload chirpstack;

    }


    public void asyncProcessPayload(HttpServletRequest req, ChipstackPayload c) {

        DelayedConversion dc = new DelayedConversion();
        dc.chirpstack = c;
      //  dc.headers = req.getHeaderNames();
        asyncConversion.add(dc);

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
                    HeliumPayload h = getHeliumPayload(w.chirpstack);

                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        log.info(mapper.writeValueAsString(h));
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                        e.printStackTrace();
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
          hh.setFrequency(c.getTxInfo().getFrequency());
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


}
