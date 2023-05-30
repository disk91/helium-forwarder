package com.disk91.forwarder.api.interfaces.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ChirpstackMetadata {
    /*
                "metadata":{
                gateway_id:"11o8R9inbpcanSdVpdQxx5G2DGtVs9UqaxxNSYMeqLG6LYf13XA"
                gateway_name:"mythical-misty-finch"
                "region_common_name":"EU868",
                "region_name":"eu868"
            }
     */

    private String gateway_id;
    private String gateway_name;
    private String region_common_name;
    private String region_name;

    // ---

    public String getRegion_common_name() {
        return region_common_name;
    }

    public void setRegion_common_name(String region_common_name) {
        this.region_common_name = region_common_name;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(String gateway_id) {
        this.gateway_id = gateway_id;
    }

    public String getGateway_name() {
        return gateway_name;
    }

    public void setGateway_name(String gateway_name) {
        this.gateway_name = gateway_name;
    }
}
