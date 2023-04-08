package com.usbank.user.management.usermanager.model;

import lombok.Data;

import java.util.Set;

@Data
public class UserSignupRequest {

    private String username;
    private String email;
    private Set<String> role;
    private String password;
    private String phoneNumber;
    private Address address;
}
