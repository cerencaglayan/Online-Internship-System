package com.g7.service;

import com.g7.config.security.JwtService;
import com.g7.exception.BadRequestException;
import com.g7.model.Company;
import com.g7.model.ConfirmationToken;
import com.g7.model.IztechUser;
import com.g7.payload.mapper.CompanyMapper;
import com.g7.payload.mapper.IztechUserMapper;
import com.g7.payload.request.CompanyCreateRequest;
import com.g7.payload.response.*;
import com.g7.repository.CompanyRepository;
import com.g7.repository.ConfirmationTokenRepository;
import com.g7.util.PasswordValidator;
import com.g7.util.PhoneNoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CompanyRepository companyRepository;
    private final PhoneNoValidator phoneNoValidator;
    private final UbysApi ubysApi;
    private final CompanyMapper mapper;
    private final JwtService jwtService;
    private final EmailService emailService;
    @Value("${frontend.url}")
    private String frontendUrl;
    private final IztechUserMapper iztechUserMapper;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenRepository tokenRepository;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Response<CompanyResponse>> register(CompanyCreateRequest request) {
        Optional<Company> maybeCompany = companyRepository.findByEmail(request.getEmail());
        if (maybeCompany.isPresent()) {
            throw new BadRequestException("Bu email adresi ile kayıtlı bir şirket zaten var.");
        }
        if (!phoneNoValidator.isValid(request.getPhoneNumber())) {
            throw new BadRequestException("Geçersiz telefon numarası.");
        }
        Company company = new Company();
        company.setEmail(request.getEmail());
        company.setName(request.getName());
        company.setPhoneNumber(request.getPhoneNumber());
        company.setAddress(request.getAddress());
        company.setIndustry(request.getIndustry());
        company.setAbout(request.getAbout());
        companyRepository.save(company);


        emailService.sendEmail(ubysApi.getInternshipCoordinators(),
                "Otamatik Staj Sistemi - Yeni Şirket Kaydı",
                request.getName() + " adlı şirket kaydoldu. Bilgilerini görüntülemek ve onaylamak için tıklayın: " + frontendUrl + "/companies");

        return Response.ok(mapper.toResponse(company), "Şirket başarıyla kaydedildi.");
    }

    public ResponseEntity<Response<SignInResponse<IztechUserResponse>>> iztechUserSignIn(String email, String password) {
        //TODO: Enable this on production
//        if (!email.contains("@std.iyte.edu.tr") && !email.contains("@iyte.edu.tr")) {
//            return Response.badRequest("Sadece IYTE e-posta adresleri kabul edilmektedir.");
//        }
        IztechUser iztechUser = ubysApi.signIn(email, password);
        if (iztechUser == null) {
            return Response.badRequest("Kullanıcı adı veya şifre hatalı.");
        }

        SignInResponse<IztechUserResponse> res = new SignInResponse<>(
                jwtService.generateToken(iztechUser, 24L),
                iztechUserMapper.toResponse(iztechUser));
        return Response.ok(res, "Giriş başarılı.");
    }

    public ResponseEntity<Response<SignInResponse<CompanyResponse>>> companySignIn(String email, String password) {
        if (email.contains("@std.iyte.edu.tr") || email.contains("@iyte.edu.tr")) {
            return Response.badRequest("Okul e-posta adresleri kabul edilmemektedir.");
        }
        Optional<Company> maybeCompany = companyRepository.findByEmail(email);
        if (maybeCompany.isEmpty()) {
            throw new BadRequestException("Bu email adresi ile kayıtlı bir şirket yok.");
        }
        Company company = maybeCompany.get();
        if (!company.getApprovalStatus()) {
            throw new BadRequestException("Şirket henüz onaylanmamış. Lütfen daha sonra tekrar deneyin.");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException authenticationException) {
            throw new BadRequestException("Şifre hatalı.");
        }
        SignInResponse<CompanyResponse> res = new SignInResponse<>(
                jwtService.generateToken(company, 24L),
                mapper.toResponse(company));

        return Response.ok(res, "Giriş başarılı.");
    }

    public ResponseEntity<Response<CompanyResponse>> activateCompany(String token, String password) {
        Optional<ConfirmationToken> maybeToken = tokenRepository.findByToken(token);
        if (maybeToken.isEmpty()) {
            throw new BadRequestException("Link geçersiz. Lütfen tekrar deneyin.");
        }
        ConfirmationToken confirmationToken = maybeToken.get();
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Link süresi dolmuş. Lütfen iletişime geçin.");
        }
        Company company = confirmationToken.getCompany();
        if (!passwordValidator.isValid(password)) {
            throw new BadRequestException("Şifre belirlenen kurallara uymuyor.");
        }
        company.setPassword(passwordEncoder.encode(password));
        companyRepository.save(company);
        tokenRepository.delete(confirmationToken);
        return Response.ok(mapper.toResponse(company), "Şirket başarıyla aktive edildi.");
    }
}
