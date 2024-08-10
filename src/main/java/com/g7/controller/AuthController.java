package com.g7.controller;

import com.g7.payload.request.*;
import com.g7.payload.response.*;
import com.g7.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<Response<CompanyResponse>> register(
            @Valid @RequestBody CompanyCreateRequest request) {
        return authService.register(request);
    }

    @PostMapping("/iztech-user/signin")
    public ResponseEntity<Response<SignInResponse<IztechUserResponse>>> iztechUserSignin(
            @RequestParam String email,
            @RequestParam String password) {
        return authService.iztechUserSignIn(email, password);
    }

    @PostMapping("/company/signin")
    public ResponseEntity<Response<SignInResponse<CompanyResponse>>> companySignIn(
            @RequestParam String email,
            @RequestParam String password) {
        return authService.companySignIn(email, password);
    }

    @PostMapping("/company/activate")
    public ResponseEntity<Response<CompanyResponse>> activateCompany(
            @RequestParam String token,
            @RequestParam String password) {
        return authService.activateCompany(token, password);
    }

}
