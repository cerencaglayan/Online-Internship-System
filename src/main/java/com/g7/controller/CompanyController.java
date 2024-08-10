package com.g7.controller;

import com.g7.payload.response.CompanyResponse;
import com.g7.payload.response.Response;
import com.g7.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    @PostMapping("/approve")
    public ResponseEntity<Response<Boolean>> approveCompany(@RequestParam Long companyId,
                                                            @RequestParam boolean approve) {
        return companyService.approveCompany(companyId, approve);
    }

    @GetMapping("/all")
    public ResponseEntity<Response<Page<CompanyResponse>>> getAllCompanies(
            @RequestParam int page,
            @RequestParam int size) {
        return companyService.getAllCompanies(page, size);
    }
}
