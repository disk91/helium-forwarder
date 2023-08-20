package com.disk91.forwarder.api.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HeliumMqttPayload extends HeliumPayload {

    /*
    {
  "app_eui": "6081F900F0008E6B",
  "dc": {
    "balance": 992414,
    "nonce": 5
  },
  "decoded": {
    "payload": {
      "data1": "coucou",
      "data2": 12.3,
      "data3": 10
    },
    "status": "success"
  },
  "dev_eui": "6081F9FC81BC54EA",
  "devaddr": "5A030048",
  "fcnt": 37940,
  "hotspots": [
    {
      "channel": 7,
      "frequency": 868.5,
      "hold_time": 0,
      "id": "11DgTdhkGCABWd2E1LdrMcSmdzZG6uRXLesw73x89Av4eMAEYDN",
      "lat": 45.855222862137865,
      "long": 3.093116556145479,
      "name": "steep-iron-tadpole",
      "reported_at": 1692528091291,
      "rssi": -91,
      "snr": 11.800000190734863,
      "spreading": "SF7BW125",
      "status": "success"
    }
  ],
  "id": "276e4d03-8073-4a94-829e-d5c1cf05d33a",
  "metadata": {
    "adr_allowed": false,
    "cf_list_enabled": false,
    "labels": [
      {
        "id": "03668b6a-7669-4a62-a1d5-a5673e44e350",
        "name": "helium_ping",
        "organization_id": "f9cf36ec-ae8a-4546-a539-fb7a6b88ebd9"
      }
    ],
    *"multi_buy": 1,
    "organization_id": "f9cf36ec-ae8a-4546-a539-fb7a6b88ebd9",
    *"preferred_hotspots": [],
    *"rx_delay": 1,
    *"rx_delay_actual": 1,
    *"rx_delay_state": "rx_delay_established"
  },
  "name": "Ping_Patrick_Pinault",
  "payload": "AmcBGAMCAVY=",
 *"payload_size": 8,
 *"raw_packet": "QFoDAEgANJQB5aiHlBsc22jPaZHj",
 *"replay": false,
 *"type": "uplink",
 *"uuid": "a1fc3a64-36e7-4971-9301-80000216d5ab"
  "port": 1,
  "reported_at": 1692528091291,
}

     */

    @Schema(description = "Size in byte of the payload", required = false)
    protected int payload_size;

    @Schema(description = "Whole packet data", required = false)
    protected String raw_packet;

    @Schema(description = "Is this packet a replay", required = false)
    protected boolean replay;

    @Schema(description = "Type of packet (uplink ...)", required = false)
    protected String type;

    @Schema(description = "UUID", required = false)
    protected String uuid;

    // ---


    public int getPayload_size() {
        return payload_size;
    }

    public void setPayload_size(int payload_size) {
        this.payload_size = payload_size;
    }

    public String getRaw_packet() {
        return raw_packet;
    }

    public void setRaw_packet(String raw_packet) {
        this.raw_packet = raw_packet;
    }

    public boolean isReplay() {
        return replay;
    }

    public void setReplay(boolean replay) {
        this.replay = replay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
