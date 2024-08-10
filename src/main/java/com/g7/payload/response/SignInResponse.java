package com.g7.payload.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignInResponse<T> {
    private String jwt;

    private T user;
}
