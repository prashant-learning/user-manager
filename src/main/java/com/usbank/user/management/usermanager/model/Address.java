package com.usbank.user.management.usermanager.model;

import lombok.Data;

@Data
public class Address {

    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String state;
    private String district;
    private String country;
    private int zipcode;

}
