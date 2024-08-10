package com.g7.payload.response;

import com.g7.model.EInternshipStatus;
import com.g7.model.FileKey;
import com.g7.model.Internship;
import com.g7.model.IztechUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
public class InternshipResponse {
    private Long id;
    private IztechUser student;
    private EInternshipStatus status;

    private CompanyResponse companyResponse;

    private LocalDateTime applicationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String sgkDocument;
    private String letterDocument;
    private String applicationForm;
    private String announcementTitle;
}
