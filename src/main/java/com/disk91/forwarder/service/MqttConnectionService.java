package com.disk91.forwarder.service;

import com.disk91.forwarder.mqtt.MqttManager;
import fr.ingeniousthings.tools.Now;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class MqttConnectionService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final long MQTT_TIMEOUT = 2*Now.ONE_HOUR; // after 2h we shut down the existing connection

    protected static class MqttConnection {
        public MqttManager mqqt = null;
    }

    private static final Object lock = new Object();
    private HashMap<String,MqttConnection> mqttConnections;

    @Autowired
    protected DownlinkService downlinkService;

    @PostConstruct
    protected void initMqttService() {
        this.mqttConnections = new HashMap<>();
    }

    /*
     * Get a corresponding Mqtt manager from the list
     * Create one if required
     */
    public MqttManager getMqttManager(
            String endpoint,
            String clientId,
            String upTopic,
            String locTopic,
            String ackTopic,
            String downTopic,
            String joinTopic,
            String format,
            int qos
    ) {
        String cId = (clientId==null)?"NULL":clientId;
        String key = endpoint+"#"+cId+"#"+upTopic+"#"+locTopic+"#"+ackTopic+"#"+joinTopic+"#"+downTopic;

        // search if exists
        MqttConnection m;
        synchronized (lock) {
            m = mqttConnections.get(key);
        }
        if ( m != null && m.mqqt.isConnected() ) {
            m.mqqt.setLastUsed(Now.NowUtcMs());
            return m.mqqt;
        } else {
            if ( m != null ) {
                // clear the previous Manager that has been disconnected
                m.mqqt.stopManager();
                mqttConnections.remove(key);
            }
            // create a new Manager
            log.info("Create a new MqttManager for {} with dntopic {}", endpoint,downTopic);
            MqttManager mm = new MqttManager(
                endpoint,
                clientId,
                format,
                upTopic,
                locTopic,
                ackTopic,
                joinTopic,
                downTopic,
                qos,
                downlinkService
            );
            if ( mm.isInitSuccess() ) {
                m = new MqttConnection();
                m.mqqt = mm;
                m.mqqt.setLastUsed(Now.NowUtcMs());
                synchronized(lock) {
                    this.mqttConnections.put(key, m);
                }
                return mm;
            }
        }
        return null;
    }

    @Scheduled(fixedDelayString = "${mqtt.clean.rate}", initialDelay = 60_000 )
    protected void cleanMqqt() {
        ArrayList<String> toRemove = new ArrayList<>();
        long now = Now.NowUtcMs();
        for ( String key : this.mqttConnections.keySet() ) {
            MqttConnection m = this.mqttConnections.get(key);
            if ( (now - m.mqqt.getLastUsed()) > MQTT_TIMEOUT ) {
                // candidate for removal
                toRemove.add(key);
            }
        }
        if (!toRemove.isEmpty()) {
            log.info("Clean connection {} mqtt connections", toRemove.size());
            synchronized (lock) {
                for (String k : toRemove) {
                    this.mqttConnections.remove(k);
                }
            }
        }
    }

}
