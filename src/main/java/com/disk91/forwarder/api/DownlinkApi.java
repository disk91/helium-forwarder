package com.disk91.forwarder.api;

import com.disk91.forwarder.api.interfaces.ActionResult;
import com.disk91.forwarder.api.interfaces.HeliumDownlink;
import com.disk91.forwarder.service.DownlinkService;
import com.disk91.forwarder.service.PayloadService;
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
@RequestMapping(value = "/forwarder/1.0/downlink")
@RestController
public class DownlinkApi {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PayloadService payloadService;

    @Autowired
    protected DownlinkService downlinkService;

    @Operation(summary = "Get a downlink message for chirpstack",
            description = "Get a downlink message for chirpstack",
            responses = {
                    @ApiResponse(responseCode = "200", description= "Done",
                            content = @Content(array = @ArraySchema(schema = @Schema( implementation = ActionResult.class)))),
            }
    )
    @RequestMapping(value="/{downlinkKey}/",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method= RequestMethod.POST)
    public ResponseEntity<?> postHeliumDownlink(
            HttpServletRequest request,
            @Parameter(required = true, name = "downlinkKey", description = "Downlink unique identified")
            @PathVariable String downlinkKey,

            @RequestBody(required = true) HeliumDownlink downlink
    ) {
        downlinkService.asyncProcessDownlink(request,downlinkKey,downlink);
        return new ResponseEntity<>(ActionResult.SUCESS(), HttpStatus.OK);
    }


}
