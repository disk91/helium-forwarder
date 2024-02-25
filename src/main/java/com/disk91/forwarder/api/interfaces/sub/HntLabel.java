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

@JsonIgnoreProperties(ignoreUnknown = true)
public class HntLabel {
    /*
    "labels":[
               {"id":"1110f814-393d-4a33-b5ec-9b69946933a3",
                "name":"test label",
                "organization_id":"f9cf36ec-ae8a-4546-a539-fb7a6b88ebd9"
               },{"id":"43fee60d-9ad2-43e8-a10b-20b8facc9815",
                 "name":"foxtrackr",
                  "organization_id":"f9cf36ec-ae8a-4546-a539-fb7a6b88ebd9"
               }],
     */

    @Schema(description = "HntLabel id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String id;
    @Schema(description = "HntLabel given name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String name;
    @Schema(description = "HntLabel organization ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String organization_id;

    // ---


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

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }
}
