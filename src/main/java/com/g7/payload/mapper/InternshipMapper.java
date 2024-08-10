package com.g7.payload.mapper;

import com.g7.model.Internship;
import com.g7.payload.response.InternshipResponse;
import com.g7.service.S3Service;
import com.g7.service.UbysApi;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InternshipMapper {
    private final CompanyMapper companyMapper;
    private final UbysApi ubysApi;
    private final S3Service s3Service;
    public InternshipResponse toInternshipResponse(Internship internship) {
        return new InternshipResponse(
                internship.getId(),
                ubysApi.getStudent(internship.getStudentMail()),
                internship.getStatus(),
                companyMapper.toResponse(internship.getCompany()),
                internship.getApplicationDate(),
                internship.getStartDate(),
                internship.getEndDate(),
                s3Service.getFileAsBase64(internship.getSgkDocument()),
                s3Service.getFileAsBase64(internship.getLetterDocument()),
                s3Service.getFileAsBase64(internship.getApplicationForm()),
                internship.getAnnouncement().getTitle()
        );
    }
}
