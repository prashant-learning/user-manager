package com.usbank.user.management.usermanager.model.request;

import com.usbank.user.management.usermanager.model.Address;
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
