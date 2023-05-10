package com.james.castlabs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.james.castlabs.exception.ErrorType;
import com.james.castlabs.exception.ServiceException;
import com.james.castlabs.model.MP4Box;
import com.james.castlabs.model.ParseRequest;
import com.james.castlabs.service.CastLabsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Validated
@RequestMapping(value = "/api/castlabs")
@Tag(name = "CastLabs API", description = "Provides parsing of MP4 boxes from MP4 files hosted at a URL.")
public class CastLabsController {
	
	@Autowired
	CastLabsService castLabsService;
    
	@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hello sent successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @Operation(summary = "Just saying hi!")
    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }
    
    
    
}
