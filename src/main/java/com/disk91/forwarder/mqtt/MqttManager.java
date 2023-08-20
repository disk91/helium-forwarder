package com.disk91.forwarder.mqtt;

import com.disk91.forwarder.api.interfaces.HeliumMqttPayload;
import com.disk91.forwarder.api.interfaces.HeliumPayload;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.ingeniousthings.tools.DateConverters;
import fr.ingeniousthings.tools.Now;
import fr.ingeniousthings.tools.RandomString;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MqttManager implements MqttCallback {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected static final int MQTT_QOS = 0;

    private MqttClient mqttClient;
    private MqttConnectOptions connectionOptions;
    private MemoryPersistence persistence;

    private String clientId;
    private String url;

    private String upTopic;
    private String downTopic;
    private String subscribeTopic;

    private boolean initSuccess = false;

    /*
     * Up/Down topic format : device_id, device_name, device_eui, app_eui, and organization_id.
     * can be parameters so basically we listen to all and have to filter
     *
     * endpoint format : mqtt(s)://user:password@server:port
     */
    public MqttManager(
            String _endpoint,
            String _clientId,
            String _upTopic,
            String _downTopic
    ) {
        int _port=0;
        String _scheme="";
        String _user="";
        String _password="";
        String _server="";
        try {
            if (_endpoint.toLowerCase().startsWith("mqtts://")) {
                _scheme = "ssl://";
                _port = 8883;
            } else if (_endpoint.toLowerCase().startsWith("mqtt://")) {
                _scheme = "tcp://";
                _port = 1883;
            } else return;

            if (!_endpoint.contains("@")) {
                _user = "";
                _password = "";
                _endpoint = _endpoint.replaceAll(".*://", "");
            } else {
                Pattern pattern = Pattern.compile(".*//(.*):(.*)@");
                Matcher matcher = pattern.matcher(_endpoint);
                if (matcher.find()) {
                    _user = matcher.group(1);
                    _password = matcher.group(2);
                } else {
                    _user = "";
                    _password = "";
                }
                _endpoint = _endpoint.replaceAll(".*@", "");
            }
            if (!_endpoint.contains(":")) {
                _server = _endpoint;
            } else {
                Pattern pattern = Pattern.compile("(.*):(.*)");
                Matcher matcher = pattern.matcher(_endpoint);
                if (matcher.find()) {
                    _server = matcher.group(1);
                    _port = Integer.parseInt(matcher.group(2));
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            log.info("Invalid mqtt server sequence: "+_endpoint);
            return;
        }


        if ( _clientId == null || _clientId.length() == 0) {
            clientId = RandomString.getRandomString(6);
        } else clientId = _clientId;
        this.url = _scheme+_server+":"+_port;
        this.upTopic = _upTopic;
        this.downTopic = _downTopic;
        if (this.downTopic != null && this.downTopic.length() > 0 ) {
            this.subscribeTopic = _downTopic.replace("{{device_id}}", "+")
                .replace("{{device_name}}", "+")
                .replace("{{device_eui}}", "+")
                .replace("{{app_eui}}", "+")
                .replace("{{organization_id}}", "+");
            // in case we had multiple "+" after this processing
            this.subscribeTopic = this.subscribeTopic.replaceAll("/.*[+].*[+].*/", "/+/");
            log.info("Downtopic: " + _downTopic + " Subscription topic: " + this.subscribeTopic);
        } else this.subscribeTopic = null;

        this.persistence = new MemoryPersistence();
        this.connectionOptions = new MqttConnectOptions();
        try {
            this.mqttClient = new MqttClient(url, clientId, persistence);
            this.connectionOptions.setCleanSession(true);
            this.connectionOptions.setAutomaticReconnect(true);
            if ( _user.length() > 0 ) this.connectionOptions.setUserName(_user);
            if ( _password.length() > 0 ) this.connectionOptions.setPassword(_password.toCharArray());
            this.connectionOptions.setConnectionTimeout(30);
            this.mqttClient.connect(this.connectionOptions);
            this.mqttClient.setCallback(this);
            if ( this.subscribeTopic != null ) {
                this.mqttClient.subscribe(this.subscribeTopic, MQTT_QOS);
            }
            log.info("New mqtt listener for "+this.url);
            this.initSuccess = true;
        } catch (MqttException me) {
            log.error("MQTT ERROR", me);
        }
    }

    public boolean isInitSuccess() {
        return initSuccess;
    }

    // stop the listener once we request a stop of the application
    public void stopManager() {
        try {
            if ( this.subscribeTopic != null ) {
                this.mqttClient.unsubscribe(this.subscribeTopic);
            }
            mqttClient.disconnect();
            mqttClient.close();
        } catch (MqttException me) {
            log.error("MQTT STOP ERROR", me);
        }
    }


    public boolean publishMessage( HeliumPayload message ) {
        try {
            String _upTopic = upTopic.replace("{{device_id}}", message.getDev_eui() )
                .replace("{{device_name}}", message.getName() )
                .replace("{{device_eui}}", message.getDev_eui() )
                .replace( "{{app_eui}}", message.getApp_eui() )
                .replace("{{organization_id}}", message.getMetadata().getOrganization_id() );

            log.info("Publish on topic ("+_upTopic+") from ("+upTopic+")");

            try {
                ObjectMapper mapper = new ObjectMapper();
                String _message = mapper.writeValueAsString(message);
                MqttMessage mqttmessage = new MqttMessage(_message.getBytes());
                mqttmessage.setQos(MQTT_QOS);
                this.mqttClient.publish(upTopic, mqttmessage);
                return true;
            } catch (JsonProcessingException x) {
                log.error("MQTT Parse exception for "+message.getDev_eui());
            }
        } catch (MqttException me) {
            log.error("MQTT Publish Error", me);
        }
        return false;
    }


    /*
     * (non-Javadoc)
     * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
     */
    @Override
    public void connectionLost(Throwable arg0) {
        // don't care, will reconnect on next message
        log.debug("MQTT - Connection Lost");
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {
        // log.info("MQTT - delivery completed");

    }

    @Override
    public void messageArrived(String topicName, MqttMessage message) throws Exception {
        // Leave it blank for Publisher
        long start = Now.NowUtcMs();
        //log.info("MQTT - MessageArrived on "+topicName);
        /*
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        */

        // some of the messages are protobuf format
        /*
        if ( topicName.matches("application/.* /event/up$") ) {
        */
            // Prefer M
    }




}
