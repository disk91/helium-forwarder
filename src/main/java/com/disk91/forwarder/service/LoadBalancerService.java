package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.api.interfaces.ActionResult;
import com.disk91.forwarder.api.interfaces.ChipstackPayload;
import fr.ingeniousthings.tools.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Service
public class LoadBalancerService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private boolean node1State = false;
    private boolean node2State = false;

    @Autowired
    protected ForwarderConfig forwarderConfig;

    @Scheduled(fixedRateString = "${balancing.check.rate}", initialDelay = 1_000)
    protected void backgroundLogStatsUpdate() {

        if ( ! forwarderConfig.isForwarderBalancerMode() ) return;

        // Test the nodes state
        node1State = isNodeActive(forwarderConfig.getForwarderBalancerNode1Enpoint());
        node2State = isNodeActive(forwarderConfig.getForwarderBalancerNode2Enpoint());

        if ( ! node1State ) log.warn("Node 1 is down");
        if ( ! node2State ) log.warn("Node 2 is down");

    }

    public boolean getNode1State() { return node1State; }
    public boolean getNode2State() { return node2State; }

    private boolean isNodeActive(String endpoint) {

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.USER_AGENT,"disk91_forwarder/1.0");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            HttpEntity he = new HttpEntity(headers);
            String url=endpoint+"/capture/state/";
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            he,
                            String.class
                    );
            if ( responseEntity.getStatusCode() == HttpStatus.OK ) {
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


    public boolean pushToNode(HttpServletRequest req, ChipstackPayload body, String event) {

        // load balancing based on deveui, select node base on
        byte eui[] = Tools.EuiStringToByteArray(body.getDeviceInfo().getDevEui());
        int q = 0;
        for ( int i = 0 ; i < 6 ; i++ ) {
            int v = (((int)eui[i]) < 0)?-eui[i]:eui[i];
            q += v;
        }
        q = q & 1;

        if ( q == 0 ) {
            if ( this.getNode1State() ) {
                // push node 1
                if ( transferPayload(forwarderConfig.getForwarderBalancerNode1Enpoint(),req,body,event) ) {
                    return true;
                }
            } else  if ( this.getNode2State() ) {
                // push node 2
                if ( transferPayload(forwarderConfig.getForwarderBalancerNode2Enpoint(),req,body,event) ) {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            if ( this.getNode2State() ) {
                // push node 2
                if ( transferPayload(forwarderConfig.getForwarderBalancerNode2Enpoint(),req,body,event) ) {
                    return true;
                }
            } else  if ( this.getNode1State() ) {
                // push node 1
                if ( transferPayload(forwarderConfig.getForwarderBalancerNode1Enpoint(),req,body,event) ) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    protected boolean transferPayload(String endpoint, HttpServletRequest req, ChipstackPayload body, String event ) {

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> ss = req.getHeaderNames();
            while (ss.hasMoreElements()) {
                String s = ss.nextElement();
                if (    s.compareToIgnoreCase(HttpHeaders.USER_AGENT) != 0
                    &&  s.compareToIgnoreCase(HttpHeaders.CONTENT_TYPE) != 0
                ) {
                    headers.add(s, req.getHeader(s));
                }
            }
            headers.add(HttpHeaders.USER_AGENT,"disk91_forwarder/1.0");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

            HttpEntity he = new HttpEntity(body,headers);
            String url=endpoint+"/capture/?event="+event;
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            he,
                            String.class
                    );
            if ( responseEntity.getStatusCode() == HttpStatus.OK ) {
                log.debug("Frame transferred to "+endpoint);
                return true;
            } else if ( responseEntity.getStatusCode() == HttpStatus.NO_CONTENT ) {
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
