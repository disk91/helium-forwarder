/*
 * Copyright (c) - Paul Pinault (aka disk91) - 2020.
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

package com.disk91.forwarder.api.interfaces.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HntHotspot {

    /*
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
     */
    @Schema(description = "Base58 encoded hotspot public key", required = false)
    protected String id;
    @Schema(description = "Uniq name of the hotspot", required = false)
    protected String name;

    @Schema(description = "Communication Channel", required = false)
    protected int channel;
    @Schema(description = "Reception frequency", required = false)
    protected double frequency;
    @Schema(description = "HntHotspot lat", required = false)
    protected double lat;
    @Schema(description = "HntHotspot lng", required = false)
    @JsonProperty("long")
    protected double lng;

    @Schema(description = "Timestamp in S", required = false)
    protected long reported_at;

    @Schema(description = "Signal rssi", required = false)
    protected double rssi;
    @Schema(description = "Signal snr", required = false)
    protected double snr;
    @Schema(description = "Spreading factor + bandwidth", required = false)
    protected String spreading;
    @Schema(description = "Status (undefined)", required = false)
    protected String status;


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

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getReported_at() {
        return reported_at;
    }

    public void setReported_at(long reported_at) {
        this.reported_at = reported_at;
    }

    public double getRssi() {
        return rssi;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }

    public double getSnr() {
        return snr;
    }

    public void setSnr(double snr) {
        this.snr = snr;
    }

    public String getSpreading() {
        return spreading;
    }

    public void setSpreading(String spreading) {
        this.spreading = spreading;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
