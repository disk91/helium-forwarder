package com.disk91.forwarder.api.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HeliumLocPayload {

    /*
        {
          "dev_eui" : "01a2b3c4d5060708",
          "app_eui" : "0102030405a6b7c8",
          "organization_id" : "1231-1315-1515315315",
          "name" : "myDevice",
          "latitude" : 42.2,
          "longitude": 3.5,
          "accuracy": 20,
          "source": "GEO_RESOLVER_GNSS",
        }
    */
    @Schema(description = "Device eui", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String dev_eui;

    @Schema(description = "Organization (Tenant) id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String organization_id;

    @Schema(description = "Application / Join eui", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String app_eui;

    @Schema(description = "Device name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String name;

    @Schema(description = "Device latitude", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected double latitude;

    @Schema(description = "Device longitude", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected double longitude;

    @Schema(description = "Device accuracy in meters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected int accuracy;

    @Schema(description = "Location source, as given by the external computation engine", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String source;

    // ---


    public String getDev_eui() {
        return dev_eui;
    }

    public void setDev_eui(String dev_eui) {
        this.dev_eui = dev_eui;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getApp_eui() {
        return app_eui;
    }

    public void setApp_eui(String app_eui) {
        this.app_eui = app_eui;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }
}
