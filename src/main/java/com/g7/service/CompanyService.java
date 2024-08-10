package com.g7.service;


import com.g7.model.Company;
import com.g7.model.ConfirmationToken;
import com.g7.payload.mapper.CompanyMapper;
import com.g7.payload.response.CompanyResponse;
import com.g7.payload.response.Response;
import com.g7.repository.CompanyRepository;
import com.g7.repository.ConfirmationTokenRepository;
import com.g7.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final EmailService emailService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    @Value("${frontend.url}")
    private String frontendUrl;
    private final CompanyMapper companyMapper;
    private final UserUtil userUtil;

    public ResponseEntity<Response<Boolean>> approveCompany(Long companyId, boolean approve) {
        Optional<Company> maybeCompany = companyRepository.findById(companyId);
        if (maybeCompany.isEmpty()) {
            return Response.badRequest("Şirket bulunamadı.");
        }
        Company company = maybeCompany.get();
        if (company.getApprovalStatus() != null) {
            return Response.badRequest("Daha önceden onaylanan/reddedilen şirketler tekrar onaylanamaz.");
        }

        company.setApprovalStatus(approve);
        companyRepository.save(company);

        if (approve) {
            LocalDateTime currentTime = LocalDateTime.now();
            ConfirmationToken confirmationToken = new ConfirmationToken();
            confirmationToken.setCompany(company);
            confirmationToken.setToken(UUID.randomUUID().toString());
            confirmationToken.setCreatedAt(currentTime);
            confirmationToken.setExpiresAt(currentTime.plusYears(4));
            confirmationToken.setTopic("company-activation");
            confirmationTokenRepository.save(confirmationToken);

            emailService.sendEmail(company.getEmail(),
                    "Otomatik Staj Sistemi - Başvuru Sonucu",
                    "Başvurunuz onaylandı. Şifrenizi belirlemek ve sisteme giriş yapmak için tıklayınız: " + frontendUrl + "/setNewPassword/" + confirmationToken.getToken());
        } else {
            emailService.sendEmail(company.getEmail(),
                    "Otomatik Staj Sistemi - Başvuru Sonucu",
                    "Başvurunuz reddedildi. Daha fazla bilgi için iletişime geçiniz.");
        }
        return Response.ok(true, "");
    }

    public ResponseEntity<Response<Page<CompanyResponse>>> getAllCompanies(int page, int size) {
        if (!userUtil.isCurrentUserInternshipCoordinator()) {
            return Response.status(HttpStatus.FORBIDDEN, null, "Bu işlemi yapmaya yetkiniz yok.");
        }
        Page<CompanyResponse> companies = companyRepository.findAll(PageRequest.of(page, size)).map(companyMapper::toResponse);
        return Response.ok(companies, "İşlem başarılı.");
    }
}
