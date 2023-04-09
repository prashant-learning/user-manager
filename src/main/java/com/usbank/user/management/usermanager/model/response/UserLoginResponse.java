package com.usbank.user.management.usermanager.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {


    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
}
