package com.usbank.user.management.usermanager.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@ToString
@Setter
@Getter
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 120)
    private String addressLine1;

    private String addressLine2;

    private String landmark;

    @NotBlank
    @Size(max = 70)
    private String state;

    @NotBlank
    @Size(max = 70)
    private String district;

    @NotBlank
    @Size(max = 50)
    private String country;

    @NotNull
    private Integer zipcode;

}
