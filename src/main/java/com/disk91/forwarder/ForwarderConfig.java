/*
 * Copyright (c) - Paul Pinault (aka disk91) - 2018.
 *
 *    Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *    and associated documentation files (the "Software"), to deal in the Software without restriction,
 *    including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *    sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *    furnished to do so, subject to the following conditions:
 *
 *    The above copyright notice and this permission notice shall be included in all copies or
 *    substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *    FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *    OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *    WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 *    IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.disk91.forwarder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
@PropertySource(value = {"file:${config.file}"}, ignoreResourceNotFound = true)
public class ForwarderConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // =====================================
    // Application Misc
    // =====================================
    @Value("${forwarder.version}")
    private String version;

    @Value("${config.file}")
    private String configFile;

    @Value ("${helium.jwt.signature.key.default}")
    private String jwtSignatureKeyDefault;

    @Value ("${helium.jwt.signature.key}")
    private String jwtSignatureKeyExternal;

    public String getJwtSignatureKeyDefault() {
        return jwtSignatureKeyDefault;
    }

    public String getJwtSignatureKeyExternal() {
        return jwtSignatureKeyExternal;
    }

    public String getJwtSignatureKey() {
        if (!this.getJwtSignatureKeyExternal().isEmpty() && this.getJwtSignatureKeyExternal().length() != 64) {
            log.error("helium.jwt.signature.key format is invalid, must be 64 char. Back to default");
        }
        if (this.getJwtSignatureKeyExternal().length() == 64) return this.getJwtSignatureKeyExternal();
        return getJwtSignatureKeyDefault();
    }


    public String getVersion() {
        return version;
    }

    public String getConfigFile() {
        return configFile;
    }


    // ==============================
    // api backend
    // ==============================

    @Value ("${helium.position.url}")
    private String HeliumPositionUrl;

    @Value ("${helium.position.user}")
    private String HeliumPositionUser;

    @Value ("${helium.position.pass}")
    private String HeliumPositionPass;

    public String getHeliumPositionUrl() {
        return HeliumPositionUrl;
    }

    public String getHeliumPositionUser() {
        return HeliumPositionUser;
    }

    public String getHeliumPositionPass() {
        return HeliumPositionPass;
    }

    // ==============================
    // payload processing
    // ==============================

    @Value ("${helium.async.processor.default}")
    private int heliumAsyncProcessorDefault;

    @Value ("${helium.async.processor:0}")
    private int heliumAsyncProcessor;

    @Value ("${helium.downlink.endpoint}")
    private String heliumDownlinkEndpoint;

    @Value ("${chirpstack.api.base}")
    private String chirpstackApiBase;

    @Value ("${chirpstack.api.admin.key}")
    private String chirpstackApiAdminKey;

    public int getHeliumAsyncProcessorDefault() {
        return heliumAsyncProcessorDefault;
    }

    public int getHeliumAsyncProcessorExternal() {
        return heliumAsyncProcessor;
    }

    public int getHeliumAsyncProcessor() {
        if ( heliumAsyncProcessor > 0 ) return heliumAsyncProcessor;
        return heliumAsyncProcessorDefault;
    }

    public String getHeliumDownlinkEndpoint() {
        return heliumDownlinkEndpoint;
    }

    public String getChirpstackApiBase() {
        return chirpstackApiBase;
    }

    public String getChirpstackApiAdminKey() {
        return chirpstackApiAdminKey;
    }

    // ==============================
    // Balancer mode
    // ==============================

    @Value ("${forwarder.balancer.mode}")
    private boolean forwarderBalancerMode;

    @Value ("${forwarder.balancer.node1.endpoint}")
    private String forwarderBalancerNode1Enpoint;

    @Value ("${forwarder.balancer.node2.endpoint}")
    private String forwarderBalancerNode2Enpoint;

    public boolean isForwarderBalancerMode() {
        return forwarderBalancerMode;
    }

    public String getForwarderBalancerNode1Enpoint() {
        return forwarderBalancerNode1Enpoint;
    }

    public String getForwarderBalancerNode2Enpoint() {
        return forwarderBalancerNode2Enpoint;
    }

    // =============================
    // GPRC
    // =============================

    @Value ("${helium.grpc.enable:false}")
    private boolean heliumGrpcEnable;

    @Value ("${helium.grpc.private.keyfile.path:}")
    private String heliumGrpcPrivateKeyfilePath;

    @Value ("${helium.grpc.public.key:}")
    private String heliumGprcPublicKey;

    @Value ("${helium.gprc.server:}")
    private String heliumGrpcServerExternal;

    @Value ("${helium.gprc.server.default}")
    private String heliumGrpcServerDefault;

    @Value ("${helium.grpc.port:}")
    private String heliumGrpcPortExternal;

    @Value ("${helium.grpc.port.default}")
    private String heliumGrpcPortDefault;

    public boolean isHeliumGrpcEnable() {
        return heliumGrpcEnable;
    }

    public String getHeliumGrpcServerExternal() {
        return heliumGrpcServerExternal;
    }

    public String getHeliumGrpcServerDefault() {
        return heliumGrpcServerDefault;
    }

    public String getHeliumGrpcPortExternal() {
        return heliumGrpcPortExternal;
    }

    public String getHeliumGrpcPortDefault() {
        return heliumGrpcPortDefault;
    }

    public String getHeliumGrpcPrivateKeyfilePath() {
        return heliumGrpcPrivateKeyfilePath;
    }
    public String getHeliumGprcPublicKey() {
        return heliumGprcPublicKey;
    }
    public String getHeliumGrpcServer() {
        if (!this.getHeliumGrpcServerExternal().isEmpty()) return this.getHeliumGrpcServerExternal();
        return this.getHeliumGrpcServerDefault();
    }

    public int getHeliumGrpcPort() {
        if (!this.getHeliumGrpcPortExternal().isEmpty()) return Integer.parseInt(this.getHeliumGrpcPortExternal());
        return Integer.parseInt(this.getHeliumGrpcPortDefault());
    }

    // =============================================
    // MQTT
    // =============================================

    @Value ("${mqtt.server:tcp://mosquitto:1883}")
    private String mqttServer;

    @Value ("${mqtt.login:}")
    private String mqttLogin;

    @Value ("${mqtt.password:}")
    private String mqttPassword;

    @Value ("${mqtt.id:}")
    private String mqttId;

    public String getMqttServer() {
        return mqttServer;
    }

    public String getMqttLogin() {
        return mqttLogin;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public String getMqttId() {
        return mqttId;
    }
}
