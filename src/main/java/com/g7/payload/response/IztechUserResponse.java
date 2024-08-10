package com.g7.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IztechUserResponse {
    private String name;
    private String surname;
    private Integer grade;
    private Double gpa;
    private String email;
    private String role;
    private String phoneNumber;
    private String studentNo;
}
