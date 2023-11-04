package com.disk91.forwarder.api.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HeliumLocPayload {

    /*
        {
          "deviceEui" : "01a2b3c4d5060708",
          "appEui" : "0102030405a6b7c8",
          "orgId" : "1231-1315-1515315315",
          "deviceName" : "myDevice",
          "latitude" : 42.2,
          "longitude": 3.5,
          "accuracy": 20,
          "source": "GEO_RESOLVER_GNSS",
        }
    */
    @Schema(description = "Device eui", required = false)
    protected String deviceEui;

    @Schema(description = "Organization (Tenant) id", required = false)
    protected String orgId;

    @Schema(description = "Application / Join eui", required = false)
    protected String appEui;

    @Schema(description = "Device name", required = false)
    protected String deviceName;

    @Schema(description = "Device latitude", required = false)
    protected double latitude;

    @Schema(description = "Device longitude", required = false)
    protected double longitude;

    @Schema(description = "Device accuracy in meters", required = false)
    protected int accuracy;

    @Schema(description = "Location source, as given by the external computation engine", required = false)
    protected String source;

    // ---


    public String getDeviceEui() {
        return deviceEui;
    }

    public void setDeviceEui(String deviceEui) {
        this.deviceEui = deviceEui;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAppEui() {
        return appEui;
    }

    public void setAppEui(String appEui) {
        this.appEui = appEui;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
