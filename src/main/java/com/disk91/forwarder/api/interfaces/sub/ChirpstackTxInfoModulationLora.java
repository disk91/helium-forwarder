package com.disk91.forwarder.api.interfaces.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ChirpstackTxInfoModulationLora {
    /*
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
     */

    private int bandwidth;
    private int spreadingFactor;
    private String codeRate;

    // ---


    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getSpreadingFactor() {
        return spreadingFactor;
    }

    public void setSpreadingFactor(int spreadingFactor) {
        this.spreadingFactor = spreadingFactor;
    }

    public String getCodeRate() {
        return codeRate;
    }

    public void setCodeRate(String codeRate) {
        this.codeRate = codeRate;
    }
}
