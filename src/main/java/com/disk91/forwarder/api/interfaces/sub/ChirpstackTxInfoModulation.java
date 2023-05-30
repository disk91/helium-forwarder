package com.disk91.forwarder.api.interfaces.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ChirpstackTxInfoModulation {
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

    private ChirpstackTxInfoModulationLora lora;

    // ---


    public ChirpstackTxInfoModulationLora getLora() {
        return lora;
    }

    public void setLora(ChirpstackTxInfoModulationLora lora) {
        this.lora = lora;
    }
}
