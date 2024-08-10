package com.g7.payload.mapper;

import com.g7.model.Company;
import com.g7.payload.response.CompanyResponse;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public CompanyResponse toResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        response.setId(company.getId());
        response.setName(company.getName());
        response.setEmail(company.getEmail());
        response.setPhoneNumber(company.getPhoneNumber());
        response.setAddress(company.getAddress());
        response.setApprovalStatus(company.getApprovalStatus());
        response.setIndustry(company.getIndustry());
        response.setAbout(company.getAbout());
        response.setRole("COMPANY");
        return response;
    }
}
