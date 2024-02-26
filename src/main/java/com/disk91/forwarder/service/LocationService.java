package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.service.itf.HotspotPosition;
import com.disk91.forwarder.service.itf.sub.LatLng;
import fr.ingeniousthings.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private ObjectCache<String, HotspotPosition> positionCache;
    @PostConstruct
    private void initLocationCacheService() {
        log.info("initLocationCacheService initialization");

        if ( ! forwarderConfig.isForwarderBalancerMode() ) {
            this.positionCache = new ObjectCache<String, HotspotPosition>("PositionCache", 200000, 36 * Now.ONE_HOUR) {
                @Override
                public void onCacheRemoval(String key, HotspotPosition obj, boolean batch, boolean last) {
                    // nothing to do, readOnly
                }

                @Override
                public void bulkCacheUpdate(List<HotspotPosition> objects) {

                }
            };
        }
    }

    @Autowired
    protected NovaService novaService;

    public HotspotPosition getHotspotPosition(String hotspotId) {
        if ( forwarderConfig.isForwarderBalancerMode() ) return null;

        // try from cache
        HotspotPosition h = this.positionCache.get(hotspotId);
        if ( h != null ) return h;

        // get from Nova GRPC
        if ( forwarderConfig.isHeliumGrpcEnable() ) {

            com.uber.h3core.util.LatLng pos = novaService.grpcGetGatewayLocation(hotspotId);
            if ( pos != null ) {
                h = new HotspotPosition();
                h.setHotspotId(hotspotId);
                LatLng _p = new LatLng();
                _p.setLat(pos.lat);
                _p.setLng(pos.lng);
                _p.setAlt(0);
                _p.setCity("");
                _p.setCountry("");
                _p.setGain(0.0);
                _p.setLastDatePosition(Now.NowUtcMs());
                h.setPosition(_p);
                h.setAnimalName(Animal.getAnimalName(hotspotId,'-'));
                return h;
            } else {
                h = getEmptyPosition();
                h.setHotspotId(hotspotId);
                return h;
            }

        } else {
            // get from API
            try {
                h = loadHotspotPosition(hotspotId);
                this.positionCache.put(h, hotspotId);
            } catch (ITNotFoundException x) {
                h = getEmptyPosition();
                h.setHotspotId(hotspotId);
                return h;
            } catch (ITParseException x) {
                h = getEmptyPosition();
                h.setHotspotId(hotspotId);
                return h;
            }
        }
        return h;
    }


    private HotspotPosition empty = null;
    protected HotspotPosition getEmptyPosition() {
        if (empty == null) {
            empty = new HotspotPosition();
            LatLng l = new LatLng();
            l.setLat(0.0);
            l.setLng(0.0);
            l.setAlt(0);
            l.setGain(1.0);
            l.setLastDatePosition(Now.NowUtcMs());
            l.setCity("");
            l.setCountry("");
            empty.setPosition(l);
            empty.setAnimalName("unknown");
        }
        return empty;
    }

    // ========================================================
    // API Backend
    // ========================================================

    @Autowired
    protected ForwarderConfig forwarderConfig;

    /**
     * Get Api Headers
     */
    private HttpEntity<String> createHeaders(boolean withAuth){

        HttpHeaders headers = new HttpHeaders();
        ArrayList<MediaType> accept = new ArrayList<>();
        accept.add(MediaType.APPLICATION_JSON);
        headers.setAccept(accept);
        headers.add(HttpHeaders.USER_AGENT,"disk91_forwarder/1.0");
        if ( withAuth && forwarderConfig.getHeliumPositionUser().length() > 2 ) {
            String auth = forwarderConfig.getHeliumPositionUser() + ":" + forwarderConfig.getHeliumPositionPass();
            byte[] encodedAuth = Base64.getEncoder().encode(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            headers.add(HttpHeaders.AUTHORIZATION, authHeader);
        }
        HttpEntity<String> he = new HttpEntity<String>(headers);
        return he;

    }


    private HotspotPosition loadHotspotPosition(String hotspotID) throws ITNotFoundException, ITParseException {

        RestTemplate restTemplate = new RestTemplate();
        String url="";
        try {
            HttpEntity<String> he;
            he = createHeaders(true);
            url = forwarderConfig.getHeliumPositionUrl();
            url = url.replace("{hs}",hotspotID);
            ResponseEntity<HotspotPosition> responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            he,
                            HotspotPosition.class
                    );
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                if (responseEntity.getBody() != null) {
                    HotspotPosition response = responseEntity.getBody();
                    return response;

                } else {
                    throw new ITNotFoundException();
                }
            } else {
                throw new ITNotFoundException();
            }
        } catch (HttpClientErrorException e) {
            log.error("Position backend communication exception :" + e.getStatusCode() + "[" + e.getMessage() + "]");
            log.error("Related to Hotspost details (1) for "+hotspotID);
            log.error("Url :"+url);
            throw new ITParseException();
        } catch (HttpServerErrorException e) {
            log.error("Position backend communication exception :" + e.getStatusCode() + "[" + e.getMessage() + "]");
            log.error("Related to Hotspost details (2) for "+hotspotID);
            throw new ITParseException();
        } catch (Exception x ) {
            log.error("Unexpected error "+x.getMessage());
            throw new ITParseException();
        }
    }


}
