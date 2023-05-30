package com.disk91.forwarder.api.interfaces;

import com.disk91.forwarder.api.interfaces.sub.ChirpstackDeviceInfo;
import com.disk91.forwarder.api.interfaces.sub.ChirpstackRxInfo;
import com.disk91.forwarder.api.interfaces.sub.ChirpstackTxInfo;
import com.disk91.forwarder.api.interfaces.sub.KeyValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChipstackPayload {

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
            "time":"2023-05-29T19:50:10+00:00",
            "rssi":-41,
            "snr":7.8,
            "context":"EbkTFA==",
            "metadata":{
                "region_common_name":"EU868",
                "region_name":"eu868"
            }
        },
        {
            "gatewayId":"3c408850a5b4f27c",
            "uplinkId":11888,
            "time":"2023-05-29T19:50:10+00:00",
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
                "codeRate":"CR_4_5"
            }
        }
    }
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
}
