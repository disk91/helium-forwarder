package com.disk91.forwarder.mqtt;

import com.disk91.forwarder.api.interfaces.ChirpstackPayload;
import com.disk91.forwarder.api.interfaces.HeliumLocPayload;
import com.disk91.forwarder.api.interfaces.HeliumMqttDownlinkPayload;
import com.disk91.forwarder.api.interfaces.HeliumPayload;
import com.disk91.forwarder.service.DownlinkService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.ingeniousthings.tools.*;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MqttManager implements MqttCallback {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected static final int MQTT_QOS = 2;

    private MqttClient mqttClient;
    private MqttConnectOptions connectionOptions;
    private MemoryPersistence persistence;

    private String clientId;
    private String format;  // Helium or Chirpstack (default Helium)
    private String url;
    private int qos;
    private String upTopic;
    private String locTopic;
    private String ackTopic;
    private String joinTopic;
    private String downTopic;
    private String subscribeTopic;

    private HashMap<String,String> deviceEuis;
    private int downlinkDevIdField;
    private boolean initSuccess = false;

    private boolean connected = false;

    protected DownlinkService downlinkService;

    protected long lastUsed = 0;
    public long getLastUsed() {
        return lastUsed;
    }
    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    /*
     * Up/Down topic format : device_id, device_name, device_eui, app_eui, and organization_id.
     * can be parameters so basically we listen to all and have to filter
     *
     * endpoint format : mqtt(s)://user:password@server:port
     */
    public MqttManager(
            String _endpoint,
            String _clientId,
            String _format,
            String _upTopic,
            String _locTopic,
            String _ackTopic,
            String _joinTopic,
            String _downTopic,
            int _qos,
            DownlinkService _downlinkService
    ) {
        int _port=0;
        String _scheme="";
        String _user="";
        String _password="";
        String _server="";
        this.downlinkService = _downlinkService;
        this.downlinkDevIdField = -1;
        this.deviceEuis=new HashMap<>();
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
            log.warn("Invalid mqtt server sequence: {}", _endpoint);
            return;
        }


        if ( _clientId == null || _clientId.isEmpty()) {
            clientId = RandomString.getRandomString(6);
        } else clientId = _clientId;
        this.url = _scheme+_server+":"+_port;
        this.format = _format.strip().toLowerCase();
        this.upTopic = _upTopic;
        this.locTopic = _locTopic;
        this.ackTopic = _ackTopic;
        this.joinTopic = _joinTopic;
        this.downTopic = _downTopic;
        this.qos = _qos;

        if (this.downTopic != null && !this.downTopic.isEmpty()) {
            // if we don't have {{device_id}} in the
            boolean haveDeviceId = downTopic.contains("/{{device_id}}/")
                || downTopic.startsWith("{{device_id}}/")
                || downTopic.endsWith("/{{device_id}}");

            // get the downlink deviceId field for later fast extraction
            String []  fields = downTopic.split("/");
            for ( int i = 0 ; i < fields.length ; i++ ) {
                if ( fields[i].compareToIgnoreCase("{{device_id}}") == 0 ) {
                    this.downlinkDevIdField = i;
                }
            }
            if ( this.downlinkDevIdField == -1 ) {
                log.warn("This is a strange situation {}", downTopic);
                haveDeviceId = false;
            }

            if ( haveDeviceId ) {
                this.subscribeTopic = downTopic.replace("{{device_id}}", "+")
                    .replace("{{device_name}}", "+")
                    .replace("{{device_eui}}", "+")
                    .replace("{{app_eui}}", "+")
                    .replace("{{organization_id}}", "+");
                // in case we had multiple "+" after this processing
                this.subscribeTopic = this.subscribeTopic.replaceAll("/.*[+].*[+].*/", "/+/");
                log.info("Downtopic: {} Subscription topic: {}", downTopic, this.subscribeTopic);
            } else {
                log.warn("We have a Mqtt setup without a supported path for {} ({})", this.url, downTopic);
            }
        } else this.subscribeTopic = null;

        this.persistence = new MemoryPersistence();
        this.connectionOptions = new MqttConnectOptions();
        try {
            this.mqttClient = new MqttClient(url, clientId, persistence);
            this.connectionOptions.setCleanSession(true);
            this.connectionOptions.setAutomaticReconnect(false);
            if (!_user.isEmpty()) this.connectionOptions.setUserName(_user);
            if (!_password.isEmpty()) this.connectionOptions.setPassword(_password.toCharArray());
            this.connectionOptions.setConnectionTimeout(5);
            this.mqttClient.connect(this.connectionOptions);
            this.mqttClient.setCallback(this);
            if ( this.subscribeTopic != null ) {
                this.mqttClient.subscribe(this.subscribeTopic, MQTT_QOS);
            }
            log.info("New mqtt listener for {}", this.url);
            this.initSuccess = true;
            this.connected = true;
        } catch (MqttException me) {
            log.error("MQTT ERROR ({}) on {}", me.getMessage(), this.url);
        }
    }

    public boolean isInitSuccess() {
        return initSuccess;
    }

    public boolean isConnected() {
        return initSuccess && connected;
    }

    // stop the listener once we request a stop of the application
    public void stopManager() {
        try {
            if ( this.initSuccess ) {
                if ( this.mqttClient.isConnected() ) {
                    if (this.subscribeTopic != null) {
                        this.mqttClient.unsubscribe(this.subscribeTopic);
                    }
                    mqttClient.disconnect();
                }
                mqttClient.close();
                this.deviceEuis.clear();
                this.deviceEuis = null;
            }
        } catch (MqttException me) {
            log.error("MQTT STOP ERROR", me);
        }
    }


    public boolean publishMessage( HeliumPayload message, ChirpstackPayload chirpMessage ) {
        if ( ! this.initSuccess || ! this.connected ) return false;
        try {

            // Add the deveui if not yet know in list for downlink control
            this.deviceEuis.computeIfAbsent(message.getDev_eui().toLowerCase(), k -> message.getDev_eui().toLowerCase());

            // Compose uplink topic
            String _upTopic = upTopic.replace("{{device_id}}", message.getDev_eui())
                    .replace("{{device_name}}", message.getName())
                    .replace("{{device_eui}}", message.getDev_eui())
                    .replace("{{app_eui}}", message.getApp_eui())
                    .replace("{{organization_id}}", message.getMetadata().getOrganization_id());

            log.debug("Publish up on topic ({}) from ({})", _upTopic, upTopic);

            int _qos = (this.qos == -1) ? MQTT_QOS : this.qos;
            try {
                ObjectMapper mapper = new ObjectMapper();
                String _message;
                if ( this.format != null && this.format.compareTo("chirpstack") == 0 ) {
                    // We want a Chirpstack Payload
                    _message = mapper.writeValueAsString(chirpMessage);
                } else {
                    _message = mapper.writeValueAsString(message);
                }
                MqttMessage mqttmessage = new MqttMessage(_message.getBytes());
                mqttmessage.setQos(_qos);
                this.mqttClient.publish(_upTopic, mqttmessage);
                return true;
            } catch (JsonProcessingException x) {
                log.error("MQTT Up Parse exception for {}", message.getDev_eui());
            }
        } catch (MqttException me) {
            log.error("MQTT Up Publish Error {}", me.getMessage());
        }
        return false;
    }

    public boolean publishAck(HeliumPayload message, ChirpstackPayload chirpMessage ) {
        if ( ! this.initSuccess || ! this.connected ) return false;
        try {
            // checks
            if ( message == null ) return true; // reject and not retry
            if ( ackTopic.isEmpty() ) return true; // reject and not retry

            String _ackTopic = ackTopic.replace("{{device_id}}", message.getDev_eui())
                    .replace("{{device_name}}", message.getName())
                    .replace("{{device_eui}}", message.getDev_eui())
                    .replace("{{app_eui}}", message.getApp_eui())
                    .replace("{{organization_id}}", message.getMetadata().getOrganization_id());

            log.debug("Publish ack on topic ({}) from ({})", _ackTopic, ackTopic);

            int _qos = ( this.qos == -1 )?MQTT_QOS:this.qos;
            try {
                ObjectMapper mapper = new ObjectMapper();
                String _message = mapper.writeValueAsString(chirpMessage);
                MqttMessage mqttmessage = new MqttMessage(_message.getBytes());
                mqttmessage.setQos(_qos);
                this.mqttClient.publish(_ackTopic, mqttmessage);
                return true;
            } catch (JsonProcessingException x) {
                log.error("MQTT Ack Parse exception for {}", chirpMessage.getDeviceInfo().getDevEui());
            }
        } catch (MqttException me) {
            log.error("MQTT Ack Publish Error", me);
        }
        return false;
    }

    public boolean publishJoin(HeliumPayload message, ChirpstackPayload chirpMessage ) {
        if ( ! this.initSuccess || ! this.connected ) return false;
        try {
            // checks
            if ( message == null ) return true; // reject and not retry

            // Add the deveui if not yet know in list for downlink control
            this.deviceEuis.computeIfAbsent(message.getDev_eui().toLowerCase(), k -> message.getDev_eui().toLowerCase());

            if ( joinTopic.isEmpty() ) return true; // reject and not retry

            String _joinTopic = joinTopic.replace("{{device_id}}", message.getDev_eui())
                    .replace("{{device_name}}", message.getName())
                    .replace("{{device_eui}}", message.getDev_eui())
                    .replace("{{app_eui}}", message.getApp_eui())
                    .replace("{{organization_id}}", message.getMetadata().getOrganization_id());

            log.debug("Publish join on topic ({}) from ({})", _joinTopic, joinTopic);

            int _qos = ( this.qos == -1 )?MQTT_QOS:this.qos;
            try {
                ObjectMapper mapper = new ObjectMapper();
                String _message = mapper.writeValueAsString(chirpMessage);
                MqttMessage mqttmessage = new MqttMessage(_message.getBytes());
                mqttmessage.setQos(_qos);
                this.mqttClient.publish(_joinTopic, mqttmessage);
                return true;
            } catch (JsonProcessingException x) {
                log.error("MQTT Join Parse exception for {}", chirpMessage.getDeviceInfo().getDevEui());
            }
        } catch (MqttException me) {
            log.error("MQTT Join Publish Error", me);
        }
        return false;
    }

    public boolean publishLocation( HeliumLocPayload message, ChirpstackPayload chirpMessage ) {
        if ( ! this.initSuccess || ! this.connected ) return false;
        try {
            // checks
            if ( message == null ) return true; // reject and not retry
            if ( locTopic.isEmpty() ) return true; // reject and not retry

            // Compose uplink topic
            String _locTopic = locTopic.replace("{{device_id}}", message.getDev_eui())
                .replace("{{device_name}}", message.getName() )
                .replace("{{device_eui}}",message.getDev_eui() )
                .replace("{{app_eui}}", message.getApp_eui())
                .replace("{{organization_id}}", message.getOrganization_id() );

            log.info("Publish loc on topic ({}) from ({})", _locTopic, locTopic);

            int _qos = ( this.qos == -1 )?MQTT_QOS:this.qos;
            try {
                ObjectMapper mapper = new ObjectMapper();
                String _message;
                if ( this.format != null && this.format.compareTo("chirpstack") == 0 ) {
                    // We want a Chirpstack Payload
                    _message = mapper.writeValueAsString(chirpMessage);
                } else {
                    _message = mapper.writeValueAsString(message);
                }
                MqttMessage mqttmessage = new MqttMessage(_message.getBytes());
                mqttmessage.setQos(_qos);
                this.mqttClient.publish(_locTopic, mqttmessage);
                return true;
            } catch (JsonProcessingException x) {
                log.error("MQTT Loc Parse exception for {}", message.getDev_eui());
            }
        } catch (MqttException me) {
            log.error("MQTT Loc Publish Error", me);
        }
        return false;
    }


    /*
     * (non-Javadoc)
     * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
     */
    @Override
    public void connectionLost(Throwable cause) {
        // don't care, will reconnect on next message
        log.debug("MQTT - Connection Lost - {}", cause.getMessage());
        this.connected = false;
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
        log.debug("MQTT - MessageArrived on {}", topicName);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);

            // Device Id comes from the topic, looks like a mess
            String[] topics = topicName.split("/");
            if (topics.length > this.downlinkDevIdField && this.downlinkDevIdField >=0 ) {
                String deviceEui = topics[this.downlinkDevIdField];
                if (Stuff.isAnHexString(deviceEui)) {
                    // verify the device Id is authorized
                    if (this.deviceEuis.get(deviceEui.toLowerCase()) != null) {

                        // process the payload
                        try {
                            HeliumMqttDownlinkPayload hmm = mapper.readValue(message.toString(), HeliumMqttDownlinkPayload.class);
                            this.setLastUsed(Now.NowUtcMs());
                            if ( hmm.getPort() == -1 ) {
                                // this is just a keep alive request for the downlink subscription
                                log.debug("Downlink keep alive received");
                            } else {
                                downlinkService.asyncProcessMqttDownlink(hmm, deviceEui);
                                log.debug("Downlink for {} registered for processing",deviceEui);
                            }
                        } catch (JsonProcessingException x) {
                            log.warn("Impossible to extract downlink payload from {}({}) skipping", this.downTopic, this.url);
                        }

                    } else {
                        // can be normal due to load balancing
                        // @TODO when balanced, we could process it twice potentially
                        // but a such case usually happen before a restart cleaning the cache
                        log.debug("Downlink from a device ({})currently unknown id {}",deviceEui,downlinkDevIdField);
                    }
                } else {
                    log.debug("Downlink DevEui format is not an hexString");
                }
            } else {
                log.debug("Downlink Can't find the devEUI field in topic");
            }
        } catch (Exception x) {
            log.warn("Exception in processing MQTT downlink {}", x.getMessage());
            x.printStackTrace();
        }
    }


}
