package com.disk91.forwarder.api;

import com.disk91.forwarder.api.interfaces.ActionResult;
import com.disk91.forwarder.api.interfaces.ChipstackPayload;
import com.disk91.forwarder.service.PayloadService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

@Tag( name = "capture api", description = "capture message from chripstack" )
@CrossOrigin
@RequestMapping(value = "/capture")
@RestController
public class CaptureApi {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PayloadService payloadService;

    @Operation(summary = "Get a message from chirpstack",
            description = "Get a message from chirpstack",
            responses = {
                    @ApiResponse(responseCode = "200", description= "Done",
                            content = @Content(array = @ArraySchema(schema = @Schema( implementation = ActionResult.class)))),
                    @ApiResponse(responseCode = "403", description= "Forbidden", content = @Content(schema = @Schema(implementation = ActionResult.class))),
            }
    )
    @RequestMapping(value="/",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method= RequestMethod.POST)
    public ResponseEntity<?> postChirpstackMessage(
            HttpServletRequest request,
            @Parameter(required = true, name = "event", description = "Get the type of event ( impacts body structure )")
            @RequestParam("event") String event,
            @RequestBody(required = true)  ChipstackPayload /* String */  message
    ) {
/*
        try {
            ObjectMapper mapper = new ObjectMapper();
            log.info("## "+mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
 */

        // events :
        //  - join
        //  - up => uplink
        //  - txack => what hotspots acked the tx
        //  - status => radio quality info
        //  - ack => confirm downlink ack
        //  - log => error
        //  - location => position
        //  - integration => related to integration

        if ( event.compareToIgnoreCase("up") != 0 ) {
            // join message or error message
            return new ResponseEntity<>(ActionResult.SUCESS(), HttpStatus.NO_CONTENT);
        }
        payloadService.asyncProcessUplink(request,message);
 //       log.info(message);
        return new ResponseEntity<>(ActionResult.SUCESS(), HttpStatus.OK);
    }


}
