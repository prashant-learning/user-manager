package com.usbank.user.management.usermanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.usbank.user.management.usermanager.model.UserSignupRequest;
import com.usbank.user.management.usermanager.model.response.UserRegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.usbank.user.management.usermanager.service.UserAuthnOperationService;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController implements UserAuthApi {

    @Autowired
    private UserAuthnOperationService userAuthnOperationService;

    @Override
    @Tag(name = "user manager")
    @Operation(summary = "Used for user registration", description = "")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "201", description = "Successfully register the user"),
                    @ApiResponse(responseCode = "409", description = "user already exist",content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody UserSignupRequest userSignupRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userAuthnOperationService.registerUser(userSignupRequest));
        } catch (JsonProcessingException e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
