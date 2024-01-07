package com.disk91.forwarder.api.interfaces.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.ingeniousthings.tools.Now;

import java.util.Calendar;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ChirpstackRxInfo {

    /*
            {
            "gatewayId":"c986398a305dee5a",
            "uplinkId":65489,
            // prior to 4.6
            "time":"2023-05-29T19:50:10+00:00",
            // from 4.6
            "gwTime":"2024-01-07T11:05:31+00:00",
            "nsTime":"2024-01-07T11:05:31.577525935+00:00

            "rssi":-41,
            "snr":7.8,
            "context":"EbkTFA==",
            crcStatus:"CRC_OK",
            "metadata":{
                "region_common_name":"EU868",
                "region_config_id":"eu868",
                "gateway_h3index" : "61105...",
                "gateway_lat" : "45.80...",
                "gateway_long" : "3.09...",
                "gateway_name" : "mythical-xxx..."
            }
        },

     */

    private String gatewayId;
    private long uplinkId;
    private String gwTime;
    private String nsTime;

    private String time;    // retro compat

    private int rssi;

    private double snr;
    private String context;

    private ChirpstackMetadata metadata;

    private String crcStatus;

    // ---


    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public long getUplinkId() {
        return uplinkId;
    }

    public void setUplinkId(long uplinkId) {
        this.uplinkId = uplinkId;
    }

    public String getGwTime() {
        return gwTime;
    }

    public void setGwTime(String gwTime) {
        this.gwTime = gwTime;
    }


    public String getNsTime() {
        return nsTime;
    }

    public void setNsTime(String nsTime) {
        this.nsTime = nsTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public double getSnr() {
        return snr;
    }

    public void setSnr(double snr) {
        this.snr = snr;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public ChirpstackMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ChirpstackMetadata metadata) {
        this.metadata = metadata;
    }

    public String getCrcStatus() {
        return crcStatus;
    }

    public void setCrcStatus(String crcStatus) {
        this.crcStatus = crcStatus;
    }
}
