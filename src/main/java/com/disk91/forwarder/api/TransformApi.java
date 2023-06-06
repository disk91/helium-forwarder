package com.disk91.forwarder.api;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.api.interfaces.ActionResult;
import com.disk91.forwarder.api.interfaces.ChipstackPayload;
import com.disk91.forwarder.api.interfaces.HeliumPayload;
import com.disk91.forwarder.service.LoadBalancerService;
import com.disk91.forwarder.service.PayloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag( name = "payload transform api", description = "tansform payload" )
@CrossOrigin
@RequestMapping(value = "/capture")
@RestController
public class TransformApi {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PayloadService payloadService;

    @Autowired
    protected ForwarderConfig forwarderConfig;


    @Operation(summary = "Transform a Chirpstack payload into Helium Payload",
            description = "Transform a Chirpstack payload into Helium Payload, enriched with location, " +
                    "fair-use rule : identify yourself with a personal user-agent, max rate 1 req / min avg",
            responses = {
                    @ApiResponse(responseCode = "200", description= "Success", content = @Content(schema = @Schema(implementation = HeliumPayload.class))),
                    @ApiResponse(responseCode = "400", description= "Bad Request", content = @Content(schema = @Schema(implementation = ActionResult.class))),
            }
    )
    @RequestMapping(value="/transform/",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method= RequestMethod.POST)
    public ResponseEntity<?> postChiprstacToHelium(
            HttpServletRequest request,
            @RequestBody(required = true)  ChipstackPayload  message
    ) {
         try {
             HeliumPayload p = payloadService.getHeliumPayload(message);
             return new ResponseEntity<>(p, HttpStatus.OK);
         } catch ( Exception x ) {
             return new ResponseEntity<>(ActionResult.BADREQUEST(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Enrich a Chirpstack payload with Gateway location",
            description = "Enrich a Chirpstack payload into Helium Payload, fair-use rule : identify yourself with a personal user-agent, max rate 1 req / min avg",
            responses = {
                    @ApiResponse(responseCode = "200", description= "Success", content = @Content(schema = @Schema(implementation = ChipstackPayload.class))),
                    @ApiResponse(responseCode = "400", description= "Bad Request", content = @Content(schema = @Schema(implementation = ActionResult.class))),
            }
    )
    @RequestMapping(value="/enrich/",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method= RequestMethod.POST)
    public ResponseEntity<?> postChiprstacEnrichement(
            HttpServletRequest request,
            @RequestBody(required = true)  ChipstackPayload message
    ) {
        try {
            ChipstackPayload p = payloadService.enrichPayload(message);
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch ( Exception x ) {
            return new ResponseEntity<>(ActionResult.BADREQUEST(), HttpStatus.BAD_REQUEST);
        }
    }



}
