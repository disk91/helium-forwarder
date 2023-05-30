package com.disk91.forwarder.api.interfaces.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ChirpstackDeviceInfo {
    /*
            "deviceInfo":{
            "tenantId":"c9316fdb-d2fe-453d-bd28-4917f7e227ce",
            "tenantName":"migration",
            "applicationId":"ace3452a-87f3-4a86-b9a8-fe8507b47ff3",
            "applicationName":"test",
            "deviceProfileId":"7fc8491b-ab6a-4871-9eb9-a57431b982a0",
            "deviceProfileName":"(EU868) Migration OTAA Without label",
            "deviceName":"disk91_test1",
            "devEui":"6081f9dde602cd71",
            "tags":{
                "label":"Without label"
            }
         },
     */

    private String tenantId;
    private String tenantName;
    private String applicationId;
    private String applicationName;
    private String deviceProfileId;
    private String deviceProfileName;
    private String deviceName;
    private String devEui;
    private KeyValue tags;

    // ---


    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getDeviceProfileId() {
        return deviceProfileId;
    }

    public void setDeviceProfileId(String deviceProfileId) {
        this.deviceProfileId = deviceProfileId;
    }

    public String getDeviceProfileName() {
        return deviceProfileName;
    }

    public void setDeviceProfileName(String deviceProfileName) {
        this.deviceProfileName = deviceProfileName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDevEui() {
        return devEui;
    }

    public void setDevEui(String devEui) {
        this.devEui = devEui;
    }

    public KeyValue getTags() {
        return tags;
    }

    public void setTags(KeyValue tags) {
        this.tags = tags;
    }
}
