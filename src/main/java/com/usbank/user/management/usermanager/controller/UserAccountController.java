package com.usbank.user.management.usermanager.controller;

import com.usbank.user.management.usermanager.model.Accounts;
import com.usbank.user.management.usermanager.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserAccountController implements UserAccountsApi {

    @Autowired
    private UserAccountService userAccountService;

    @Override
    @Tag(name = "user account details")
    @Operation(summary = "Used for getting account", description = "")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Successful login"),
                    @ApiResponse(responseCode = "404", description = "user doesn't exist",content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    @Parameters(
            {
                    @Parameter(name = "Authorization", description = "This is token", in = ParameterIn.HEADER, required = true )

            }
    )
    @GetMapping("/{username}")
    public ResponseEntity<List<Accounts>> getUserAccountDetails(@PathVariable String username, @RequestHeader(required = true) String Authorization) {

        return ResponseEntity.status(HttpStatus.OK).body(userAccountService.getAccountsByUsername(username));
    }

}
