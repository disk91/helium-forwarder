package com.disk91.forwarder.api.interfaces;

import com.disk91.forwarder.api.interfaces.sub.HntDcBalance;
import com.disk91.forwarder.api.interfaces.sub.HntDecoded;
import com.disk91.forwarder.api.interfaces.sub.HntHotspot;
import com.disk91.forwarder.api.interfaces.sub.HntMetadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HeliumPayload {

    /*
    {
        "app_eui":"52DF791129AC5AAC",
        "dc":{"balance":7630,"nonce":1},
        // Quand fonction
        "decoded":{
         "payload":{
             "altitude":1000,
             "latitude":0.25,
             "longitude":1.41,
             "sats":4
            },
         "status":"success"
        },
        "dev_eui":"756404B9D784EF5A",
        "devaddr":"11000048",
        "downlink_url":"https://console.helium.com/api/v1/down/cad3afec-8d97-4a43-8ed6-deaa51bac76c/k9F6E4BHqPS6TgyN3bH3n4LEqDJ-fUhT/f13b86e8-5f06-49f3-b251-8784f0b3ffbe",
        "fcnt":825,
        "hotspots":[
            {"channel":1,"frequency":868.2999877929688,
              "id":"11QxjZpR4Xbzb6mpjGo1F9mXLzbCNgDyteqjduSqJUmTarWnyx1",
              "lat":45.787551396227066,
              "long":3.112040492518425,
              "name":"creamy-cider-mammoth",
              "reported_at":1604750469,
              "rssi":-87.0,
              "snr":9.5,
              "spreading":"SF7BW125",
              "status":"success"
            }],
        "id":"f13b86e8-5f06-49f3-b251-8784f0b3ffbe",
        "metadata":{
           "labels":[
               {"id":"1110f814-393d-4a33-b5ec-9b69946933a3",
                "name":"test label",
                "organization_id":"f9cf36ec-ae8a-4546-a539-fb7a6b88ebd9"
               },{"id":"43fee60d-9ad2-43e8-a10b-20b8facc9815",
                 "name":"foxtrackr",
                  "organization_id":"f9cf36ec-ae8a-4546-a539-fb7a6b88ebd9"
               }],
            "organization_id":"f9cf36ec-ae8a-4546-a539-fb7a6b88ebd9",
            *"multi_buy": 1,
            *"rx_delay": 1,
            *"rx_delay_actual": 1,
            *"rx_delay_state": "rx_delay_established"
        },
        "name":"Test",
        "payload":"SGVsbG8gT1RBQSE=",
        *"payload_size": 8,
        *"raw_packet": "QFoDAEgANJQB5aiHlBsc22jPaZHj",
        *"replay": false,
        *"type": "uplink",
        *"uuid": "a1fc3a64-36e7-4971-9301-80000216d5ab"
        "port":1,
        "reported_at":1604750469
      }
     */

    @Schema(description = "APP EUI (random on Hellium) 8 bytes / 16 hexchar", required = false)
    protected String app_eui;
    @Schema(description = "DEV EUI (random on Hellium) 8 bytes / 16 hexchar", required = false)
    protected String dev_eui;
    @Schema(description = "Session DEVADR (random on Hellium) 4 bytes / 8 hexchar ", required = false)
    protected String devaddr;
    @Schema(description = "Helium device UUID", required = false)
    protected String id;
    @Schema(description = "Helium device Name", required = false)
    protected String name;

    @Schema(description = "Payload decoded data", required = false)
    protected HntDecoded decoded;

    @Schema(description = "Sequence Id", required = false)
    protected int fcnt;
    @Schema(description = "Communication port", required = false)
    protected int port;
    @Schema(description = "Base64 binary encoded payload", required = false)
    protected String payload;

    @Schema(description = "Message timestamp in S", required = false)
    protected long reported_at;

    @Schema(description = "Account DC balance", required = false)
    protected HntDcBalance dc;

    @Schema(description = "Address to be use to request a downlink", required = false)
    protected String downlink_url;

    @Schema(description = "List of Hotspots receiving the information", required = false)
    protected List<HntHotspot> hotspots;

    @Schema(description = "Associated meta data", required = false)
    protected HntMetadata metadata;

    // ---


    public String getApp_eui() {
        return app_eui;
    }

    public void setApp_eui(String app_eui) {
        this.app_eui = app_eui;
    }

    public String getDev_eui() {
        return dev_eui;
    }

    public void setDev_eui(String dev_eui) {
        this.dev_eui = dev_eui;
    }

    public String getDevaddr() {
        return devaddr;
    }

    public void setDevaddr(String devaddr) {
        this.devaddr = devaddr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFcnt() {
        return fcnt;
    }

    public void setFcnt(int fcnt) {
        this.fcnt = fcnt;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public long getReported_at() {
        return reported_at;
    }

    public void setReported_at(long reported_at) {
        this.reported_at = reported_at;
    }

    public HntDcBalance getDc() {
        return dc;
    }

    public void setDc(HntDcBalance dc) {
        this.dc = dc;
    }

    public String getDownlink_url() {
        return downlink_url;
    }

    public void setDownlink_url(String downlink_url) {
        this.downlink_url = downlink_url;
    }

    public List<HntHotspot> getHotspots() {
        return hotspots;
    }

    public void setHotspots(List<HntHotspot> hotspots) {
        this.hotspots = hotspots;
    }

    public HntMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(HntMetadata metadata) {
        this.metadata = metadata;
    }

    public HntDecoded getDecoded() {
        return decoded;
    }

    public void setDecoded(HntDecoded decoded) {
        this.decoded = decoded;
    }
}
