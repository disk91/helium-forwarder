package com.disk91.forwarder.api.interfaces.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ChirpstackTxInfo {
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

    private long frequency;
    private ChirpstackTxInfoModulation modulation;

    // ---


    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public ChirpstackTxInfoModulation getModulation() {
        return modulation;
    }

    public void setModulation(ChirpstackTxInfoModulation modulation) {
        this.modulation = modulation;
    }
}
