package com.usbank.user.management.usermanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.usbank.user.management.usermanager.model.request.UserLoginRequest;
import com.usbank.user.management.usermanager.model.request.UserSignupRequest;
import com.usbank.user.management.usermanager.model.response.UserLoginResponse;
import com.usbank.user.management.usermanager.model.response.UserRegistrationResponse;
import org.springframework.http.ResponseEntity;

public interface UserAuthApi {

    public ResponseEntity<UserRegistrationResponse> registerUser(UserSignupRequest userSignupRequest) throws JsonProcessingException;

    public ResponseEntity<UserLoginResponse> loginUser(UserLoginRequest userLoginRequest);
}
