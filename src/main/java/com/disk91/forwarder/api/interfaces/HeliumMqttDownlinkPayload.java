package com.disk91.forwarder.api.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HeliumMqttDownlinkPayload  extends HeliumDownlink {


}
