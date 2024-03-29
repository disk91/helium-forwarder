package com.disk91.forwarder.api.interfaces;

import com.disk91.forwarder.api.interfaces.sub.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChirpstackPayload {

    /*
    {
        "deduplicationId":"49403db7-8722-49b4-8d49-c3b119c534e5",
        "time":"2023-05-29T19:50:10+00:00",
        "deviceInfo":{
            "tenantId":"c9316fdb-d2fe-453d-bd28-4917f7e227ce",
            "tenantName":"migration",
            "applicationId":"ace3452a-87f3-4a86-b9a8-fe8507b47ff3",
            "applicationName":"test",
            "deviceProfileId":"7fc8491b-ab6a-4871-9eb9-a57431b982a0",
            "deviceProfileName":"(EU868) Migration OTAA Without label",
            "deviceName":"disk91_test1",
            "devEui":"6081f9dde602cd71",
            "deviceClassEnabled":"CLASS_A"
            "tags":{
                "label":"Without label"
            }
         },
        "devAddr":"480007a0",
        "dr":3,
        "adr":false,
        "fCnt":22772,
        "fPort":1,
        "confirmed":false,
        "data":"HQ==",
        "object":{
            "other":"123",
            "temp":22.5
        },
        "rxInfo":[
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
                "location" : {
                },
                "context":"EbkTFA==",
                "metadata":{
                    "region_common_name":"EU868",
                    "region_config_id":"eu868",
                    "gateway_h3index" : "61105...",
                    "gateway_lat" : "45.80...",
                    "gateway_long" : "3.09...",
                    "gateway_name" : "mythical-xxx..."
                    "gateway_id":"11o8R9inbpc...3XA",
                    "lat": 0.0,
                    "lon": 0.0,
                },
                "crcStatus":"CRC_OK",
            },
            {
                "gatewayId":"3c408850a5b4f27c",
                "uplinkId":11888,
                "time":null,
                "gwTime":"2024-02-27T18:07:11+00:00",
                "nsTime":"2024-02-27T18:07:11.115026956+00:00",
                "rssi":-22,
                "snr":8.0,
                "context":"yIKGwQ==",
                "metadata":{
                    "region_common_name":"EU868",
                    "region_name":"eu868"
                }
            }
        ],
        "txInfo":{
            "frequency":867500000,
            "modulation":{
                "lora":{
                    "bandwidth":125000,
                    "spreadingFactor":9,
                    "codeRate":"CR_4_5",
                    "polarizationInversion":false
                }
            }
        },
    }

    // when some error

    "level":"ERROR",
    "code":"OTAA",
    "description":"DevNonce has already been used",
    "context":{
        "deduplication_id":"434fd441-a36c-4109-ad8d-58bbc4263092"
    }

    // when Join

    rxInfo and txInfo are empty

    // when location
    location {
        latitude:45.291898
        longitude:-75.86377
        source:"GEO_RESOLVER_GNSS"
        accuracy:20
    }

     */


    private String deduplicationId;
    private String time;
    private ChirpstackDeviceInfo deviceInfo;
    private String devAddr;
    private int dr;
    private int fCnt;
    private int fPort;
    private String data;
    private boolean confirmed;
    private boolean adr;

    private KeyValue object;

    private List<ChirpstackRxInfo> rxInfo;

    private ChirpstackTxInfo txInfo;

    // Log message
    private String level;
    private String code;
    private String description;

    // status message

    private int margin=0;
    private double batteryLevel=0.0;

    // ack Message (for downlink)

    private String queueItemId="";
    private boolean acknowledged=false;
    private int fCntDown=0;

    // txAck
    // uses queueItemId, fCntDown
    private String gatewayId;

    // location Message
    private ChirpstackLocation location;

    // ---

    public String getDeduplicationId() {
        return deduplicationId;
    }

    public void setDeduplicationId(String deduplicationId) {
        this.deduplicationId = deduplicationId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ChirpstackDeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(ChirpstackDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDevAddr() {
        return devAddr;
    }

    public void setDevAddr(String devAddr) {
        this.devAddr = devAddr;
    }

    public int getDr() {
        return dr;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public int getfCnt() {
        return fCnt;
    }

    public void setfCnt(int fCnt) {
        this.fCnt = fCnt;
    }

    public int getfPort() {
        return fPort;
    }

    public void setfPort(int fPort) {
        this.fPort = fPort;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public List<ChirpstackRxInfo> getRxInfo() {
        return rxInfo;
    }

    public void setRxInfo(List<ChirpstackRxInfo> rxInfo) {
        this.rxInfo = rxInfo;
    }

    public ChirpstackTxInfo getTxInfo() {
        return txInfo;
    }

    public void setTxInfo(ChirpstackTxInfo txInfo) {
        this.txInfo = txInfo;
    }

    public boolean isAdr() {
        return adr;
    }

    public void setAdr(boolean adr) {
        this.adr = adr;
    }

    public KeyValue getObject() {
        return object;
    }

    public void setObject(KeyValue object) {
        this.object = object;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getQueueItemId() {
        return queueItemId;
    }

    public void setQueueItemId(String queueItemId) {
        this.queueItemId = queueItemId;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public int getfCntDown() {
        return fCntDown;
    }

    public void setfCntDown(int fCntDown) {
        this.fCntDown = fCntDown;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public ChirpstackLocation getLocation() {
        return location;
    }

    public void setLocation(ChirpstackLocation location) {
        this.location = location;
    }
}
