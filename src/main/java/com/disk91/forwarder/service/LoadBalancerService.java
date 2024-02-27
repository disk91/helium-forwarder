package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.api.interfaces.ChirpstackPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ingeniousthings.tools.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
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
            HttpEntity<String> he = new HttpEntity<>(headers);
            String url=endpoint+"/capture/state/";
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            he,
                            String.class
                    );
            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            return false;
        } catch (HttpServerErrorException e) {
            return false;
        } catch (Exception x ) {
            return false;
        }

    }


    public boolean pushToNode(HttpServletRequest req, ChirpstackPayload body, String event) {

        // load balancing based on deveui, select node base on
        byte[] eui = Tools.EuiStringToByteArray(body.getDeviceInfo().getDevEui());
        int q = 0;
        for ( int i = 0 ; i < 6 ; i++ ) {
            int v = (((int)eui[i]) < 0)?-eui[i]:eui[i];
            q += v;
        }
        q = q & 1;

        if ( q == 0 ) {
            if ( this.getNode1State() ) {
                // push node 1
                return transferPayload(forwarderConfig.getForwarderBalancerNode1Enpoint(), req, body, event);
            } else  if ( this.getNode2State() ) {
                // push node 2
                return transferPayload(forwarderConfig.getForwarderBalancerNode2Enpoint(), req, body, event);
            } else {
                return false;
            }
        } else {
            if ( this.getNode2State() ) {
                // push node 2
                return transferPayload(forwarderConfig.getForwarderBalancerNode2Enpoint(), req, body, event);
            } else  if ( this.getNode1State() ) {
                // push node 1
                return transferPayload(forwarderConfig.getForwarderBalancerNode1Enpoint(), req, body, event);
            } else {
                return false;
            }
        }
    }

    protected boolean transferPayload(String endpoint, HttpServletRequest req, ChirpstackPayload body, String event ) {

        RestTemplate restTemplate = new RestTemplate();
        try {
            /*
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

            ObjectMapper mapper = new ObjectMapper();
            String sBody = mapper.writeValueAsString(body);
            log.info("### > "+sBody);

            /*
            // HttpEntity<ChirpstackPayload> he = new HttpEntity<>(body,headers);
            HttpEntity<String> he = new HttpEntity<>(sBody,headers);
            String url=endpoint+"/capture/?event="+event;
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            he,
                            String.class
                    );
*/
            RestClient restClient = RestClient.builder()
                .baseUrl(endpoint+"/capture/?event="+event)
                .defaultHeaders( httpHeaders -> {
                    Enumeration<String> _ss = req.getHeaderNames();
                    while (_ss.hasMoreElements()) {
                        String s = _ss.nextElement();
                        if (    s.compareToIgnoreCase(HttpHeaders.USER_AGENT) != 0
                            &&  s.compareToIgnoreCase(HttpHeaders.CONTENT_TYPE) != 0
                        ) {
                            httpHeaders.set(s, req.getHeader(s));
                            log.info("Head: "+s+" / "+ req.getHeader(s));
                        }
                    }
                    httpHeaders.set(HttpHeaders.USER_AGENT,"disk91_forwarder/1.0");
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json");
                })
                .build();

            ResponseEntity<Void> responseEntity = restClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();

            if ( responseEntity.getStatusCode() == HttpStatus.OK ) {
                log.debug("Frame transferred to "+endpoint);
                return true;
            } else return responseEntity.getStatusCode() == HttpStatus.NO_CONTENT;
        } catch (HttpClientErrorException e) {
            log.info("HttpClientErrorException "+e.getMessage());
            return false;
        } catch (HttpServerErrorException e) {
            log.info("HttpServerErrorException "+e.getMessage());
            return false;
        } catch (Exception e ) {
            log.info("Exception "+e.getMessage());
            return false;
        }

    }


}
