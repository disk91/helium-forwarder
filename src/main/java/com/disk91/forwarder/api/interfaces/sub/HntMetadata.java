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
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HntMetadata {

    /*
    "metadata":{
           "labels":[
               {"id":"1110f814-393d-4a33-b5ec-9b69946933a3",
                "name":"test label",
                "organization_id":"f9cf36ec-ae8a-4546-a539-fb7a6b88ebd9"
               },{"id":"43fee60d-9ad2-43e8-a10b-20b8facc9815",
                 "name":"foxtrackr",
                  "organization_id":"f9cf36ec-ae8a-4546-a539-fb7a6b88ebd9"
               }],
            "organization_id":"f9cf36ec-ae8a-4546-a539-fb7a6b88ebd9"
            *"multi_buy": 1,
            *"rx_delay": 1,
            *"rx_delay_actual": 1,
            *"rx_delay_state": "rx_delay_established"
        },
     */

    @Schema(description = "List of labels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected List<HntLabel> labels;

    @Schema(description = "Organization Id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String organization_id;

    @Schema(description = "Multi buy configuration", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected int multi_buy;

    @Schema(description = "Rx Delay", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected int rx_delay;

    @Schema(description = "Rx Delay currently negociated with device", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected int rx_delay_actual;

    @Schema(description = "Status of rx delay negotiation with device", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String rx_delay_state;


    // ---

    public List<HntLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<HntLabel> labels) {
        this.labels = labels;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public int getMulti_buy() {
        return multi_buy;
    }

    public void setMulti_buy(int multi_buy) {
        this.multi_buy = multi_buy;
    }

    public int getRx_delay() {
        return rx_delay;
    }

    public void setRx_delay(int rx_delay) {
        this.rx_delay = rx_delay;
    }

    public int getRx_delay_actual() {
        return rx_delay_actual;
    }

    public void setRx_delay_actual(int rx_delay_actual) {
        this.rx_delay_actual = rx_delay_actual;
    }

    public String getRx_delay_state() {
        return rx_delay_state;
    }

    public void setRx_delay_state(String rx_delay_state) {
        this.rx_delay_state = rx_delay_state;
    }
}
