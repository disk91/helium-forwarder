package com.disk91.forwarder.api;

import com.disk91.forwarder.ForwarderConfig;
import com.disk91.forwarder.api.interfaces.ActionResult;
import com.disk91.forwarder.api.interfaces.ChirpstackPayload;
import com.disk91.forwarder.service.LoadBalancerService;
import com.disk91.forwarder.service.PayloadService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import jakarta.servlet.http.HttpServletRequest;

@Tag( name = "capture api & transform", description = "capture message from chripstack" )
@CrossOrigin
@RequestMapping(value = "/capture")
@RestController
public class CaptureApi {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PayloadService payloadService;

    @Autowired
    protected ForwarderConfig forwarderConfig;

    @Autowired
    protected LoadBalancerService loadBalancerService;

    @Operation(summary = "Get the status of the endpoint for load balancing",
            description = "Get status of th endpoint for load balancing",
            responses = {
                    @ApiResponse(responseCode = "200", description= "Service Open", content = @Content(schema = @Schema(implementation = ActionResult.class))),
                    @ApiResponse(responseCode = "204", description= "Service Closing", content = @Content(schema = @Schema(implementation = ActionResult.class))),
            }
    )
    @RequestMapping(value="/state/",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method= RequestMethod.GET)
    public ResponseEntity<?> getEndpointState(
            HttpServletRequest request
    ) {
        if ( payloadService.isStateClose() ) {
            return new ResponseEntity<>(ActionResult.SUCESS(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ActionResult.SUCESS(), HttpStatus.OK);
    }


    @Operation(summary = "Get a message from chirpstack",
            description = "Get a message from chirpstack",
            responses = {
                    @ApiResponse(responseCode = "200", description= "Success", content = @Content(schema = @Schema(implementation = ActionResult.class))),
                    @ApiResponse(responseCode = "204", description= "No Content", content = @Content(schema = @Schema(implementation = ActionResult.class))),
                    @ApiResponse(responseCode = "503", description= "Not Available", content = @Content(schema = @Schema(implementation = ActionResult.class))),
            }
    )
    @RequestMapping(value="/",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method= RequestMethod.POST)
    public ResponseEntity<?> postChirpstackMessage(
            HttpServletRequest request,
            @Parameter(required = true, name = "event", description = "Get the type of event ( impacts body structure )")
            @RequestParam("event") String event,
            @RequestBody(required = true) ChirpstackPayload /* String  smessage */ message
    ) {

        log.debug("Frame received, type "+event);

        /* -- for tracing input when adding new event type * /
        log.info(smessage);
        ChirpstackPayload message;
        try {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(smessage, ChirpstackPayload.class);
        } catch (JsonProcessingException e) {
            log.error("Error in parsing payload for "+smessage);
            message = new ChirpstackPayload();
        }
        / * */


        if ( forwarderConfig.isForwarderBalancerMode() ) {

            if (    event.compareToIgnoreCase("up") == 0
                 || event.compareToIgnoreCase("location") == 0
            ) {
                if ( loadBalancerService.pushToNode(request,message,event) ) {
                    return new ResponseEntity<>(ActionResult.SUCESS(), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(ActionResult.SUCESS(), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(ActionResult.FAILED(), HttpStatus.SERVICE_UNAVAILABLE);

        } else {

            // events :
            //  - join
            //  - up => uplink
            //  - txack => what hotspots acked the tx
            //  - status => radio quality info
            //  - ack => confirm downlink ack
            //  - log => error
            //  - location => position
            //  - integration => related to integration

            if ( event.compareToIgnoreCase("up") == 0
              || event.compareToIgnoreCase("location") == 0
            ) {
                payloadService.asyncProcessEvent(request,message, event);
                return new ResponseEntity<>(ActionResult.SUCESS(), HttpStatus.OK);
            }
            // not an uplink message or location message
            return new ResponseEntity<>(ActionResult.SUCESS(), HttpStatus.NO_CONTENT);
        }

    }



}
