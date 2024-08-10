package com.g7.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyResponse {

    private Long id;
    private String name;
    private String email;

    private String phoneNumber;

    private String address;

    private Boolean approvalStatus;

    private String industry;

    private String about;
    private String role;
}
