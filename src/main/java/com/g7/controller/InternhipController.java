package com.g7.controller;

import com.g7.exception.BadRequestException;
import com.g7.model.*;
import com.g7.payload.mapper.InternshipMapper;
import com.g7.payload.response.InternshipResponse;
import com.g7.payload.response.Response;
import com.g7.repository.AnnouncementRepository;
import com.g7.repository.CompanyRepository;
import com.g7.repository.FileKeyRepository;
import com.g7.repository.InternshipRepository;
import com.g7.service.EmailService;
import com.g7.service.FileService;
import com.g7.service.S3Service;
import com.g7.service.UbysApi;
import com.g7.spec.InternshipSpec;
import com.g7.util.SpecUtils;
import com.g7.util.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/internship")
public class InternhipController {
    private final InternshipRepository internshipRepository;
    private final UserUtil userUtil;
    private final CompanyRepository companyRepository;
    private final AnnouncementRepository announcementRepository;
    private final EmailService emailService;
    private final S3Service s3Service;
    private final FileKeyRepository fileKeyRepository;
    private final UbysApi ubysApi;
    private final InternshipMapper internshipMapper;
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<Response<Page<InternshipResponse>>> getInternships(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Sort.Direction order ){
        Specification<Internship> spec = Specification.where(null);
        if (userUtil.isCurrentUserCompany()) {
            spec = SpecUtils.and(SpecUtils.and(spec,
                    InternshipSpec.byCompany(userUtil.getCurrentCompany().getId())));
        } else if ( userUtil.isCurrentUserIztechStudent()){
            spec = SpecUtils.and(SpecUtils.and(spec,
                    InternshipSpec.byStudent(userUtil.getCurrentIztechUser().getEmail())));
        }
        return Response.ok(internshipRepository.findAll(spec, getPageable(page, size, sortBy, order)).map(internshipMapper::toInternshipResponse),
                "Internship list retrieved successfully");
    }

    @PostMapping("/apply")
    public ResponseEntity<Response<InternshipResponse>> applyForInternship(
            @RequestParam(name = "announcement-id") Long announcementId) {
        IztechUser currentIztechUser = userUtil.getCurrentIztechUser();

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new BadRequestException("Announcement is not found"));
        Company company = announcement.getCompany();

        internshipRepository
                .findOne(SpecUtils.and(InternshipSpec.byStudent(currentIztechUser.getEmail()),
                        InternshipSpec.byCompany(company.getId())))
                .ifPresent(internship -> {throw new BadRequestException("You have already applied internship for this company");});


        Internship internship = new Internship();
        internship.setStudentMail(currentIztechUser.getEmail());
        internship.setStatus(EInternshipStatus.STUDENT_APPLIED);
        internship.setCompany(announcement.getCompany());
        internship.setApplicationDate(LocalDateTime.now());
        internship.setAnnouncement(announcement);
        internshipRepository.save(internship);

        File file = fileService.fillLetter(currentIztechUser, internship);
        if (file != null) {
            s3Service.uploadFileTos3bucket(file.getName(), file);
            FileKey fileKey = new FileKey();
            fileKey.setPath(file.getName());
            fileKey.setS3BaseUrl("test");
            fileKeyRepository.save(fileKey);
            internship.setLetterDocument(fileKey);
            internshipRepository.save(internship);

        } else {
            throw new BadRequestException("Letter document could not be created");
        }



        File applicationForm = fileService.fillApplicationForm(currentIztechUser, internship);
        s3Service.uploadFileTos3bucket(applicationForm.getName(), applicationForm);

        FileKey applicationFormKey = new FileKey();
        applicationFormKey.setPath(applicationForm.getName());
        applicationFormKey.setS3BaseUrl("test");
        fileKeyRepository.save(applicationFormKey);

        internship.setApplicationForm(applicationFormKey);
        internshipRepository.save(internship);

        emailService.sendEmail(announcement.getCompany().getEmail(),
                "Internship Application",
                "You have a new internship application from " + currentIztechUser.getName() + " " + currentIztechUser.getSurname());

        emailService.sendEmail(currentIztechUser.getEmail(),
                "Internship Application Received",
                "You have successfully applied for the internship");
        return Response.ok(internshipMapper.toInternshipResponse(internship), "Internship application is successful");
    }

    @PostMapping("/approve-company")
    public ResponseEntity<Response<InternshipResponse>> approveCompany(
            @RequestParam(name = "internship-id") Long internshipId,
            @RequestParam Boolean approve,
            @RequestPart(required = false) MultipartFile applicationForm) {
        if (!userUtil.isCurrentUserCompany()) {
            throw new BadRequestException("You are not allowed to approve company");
        }
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new BadRequestException("Internship is not found"));
        if (approve) {
            internship.setStatus(EInternshipStatus.COMPANY_APPROVED);
            emailService.sendEmail(internship.getCompany().getEmail(),
                    "About Internship Application",
                    "Your application for the internship is being approved by the company.");

            emailService.sendEmail(ubysApi.getInternshipCoordinators(), "Waiting Internship Application",
                    "New internship application is waiting your approval");
        } else {
            internship.setStatus(EInternshipStatus.COMPANY_REJECTED);
            emailService.sendEmail(internship.getCompany().getEmail(),
                    "About Internship Application",
                    "Your application for the internship is being rejected by the company.");
        }
        internshipRepository.save(internship);

        if (applicationForm != null) {
            String applicationFormDocument = s3Service.uploadFile(applicationForm);
            FileKey fileKey = new FileKey();
            fileKey.setPath(applicationFormDocument);
            fileKey.setS3BaseUrl("test");
            if (internship.getApplicationForm() != null){
                fileKey.setPreviousVersion(internship.getApplicationForm());
            }
            fileKeyRepository.save(fileKey);

            internship.setApplicationForm(fileKey);
            internshipRepository.save(internship);
        }
        return Response.ok(internshipMapper.toInternshipResponse(internship), "Company is approved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<InternshipResponse>> getInternships(
            @PathVariable long id ){

        Optional<Internship> maybeInternship = internshipRepository.findById(id);
        return maybeInternship.map(internship -> Response.ok(internshipMapper.toInternshipResponse(internship), "The internship:")).orElseGet(() -> Response.badRequest("Not a valid Id"));

    }

    @PostMapping("/approve-coordinator")
    public ResponseEntity<Response<InternshipResponse>> uploadSgk(
            @RequestParam(name = "internship-id") Long internshipId,
            @RequestParam Boolean approve,
            @RequestPart(required = false) MultipartFile file,
            @RequestPart(required = false) MultipartFile applicationForm) {
        if (!userUtil.isCurrentUserInternshipCoordinator()) {
            throw new BadRequestException("You are not allowed to upload SGK document");
        }
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new BadRequestException("Internship is not found"));

        if (internship.getStatus() != EInternshipStatus.COMPANY_APPROVED) {
            throw new BadRequestException("You can't change status of this internship.");
        }

        if (approve) {
            if (file != null) {
                String sgkDocument = s3Service.uploadFile(file);
                FileKey fileKey = new FileKey();
                fileKey.setPath(sgkDocument);
                fileKey.setS3BaseUrl("test");
                fileKeyRepository.save(fileKey);

                internship.setSgkDocument(fileKey);
                internshipRepository.save(internship);
            }
            internship.setStatus(EInternshipStatus.INTERNSHIP_COORDINATOR_APPROVED);
            emailService.sendEmail(internship.getStudentMail(),
                    "About Internship Application",
                    "Your application for the internship is being approved by the internship coordinator.");
        } else {
            internship.setStatus(EInternshipStatus.INTERNSHIP_COORDINATOR_REJECTED);
            emailService.sendEmail(internship.getStudentMail(),
                    "About Internship Application",
                    "Your application for the internship is being rejected by the internship coordinator.");
        }

        if (applicationForm != null) {
            String applicationFormDocument = s3Service.uploadFile(applicationForm);
            FileKey fileKey = new FileKey();
            fileKey.setPath(applicationFormDocument);
            fileKey.setS3BaseUrl("test");
            if (internship.getApplicationForm() != null){
                fileKey.setPreviousVersion(internship.getApplicationForm());
            }
            fileKeyRepository.save(fileKey);

            internship.setApplicationForm(fileKey);
            internshipRepository.save(internship);
        }

        internshipRepository.save(internship);
        return Response.ok(internshipMapper.toInternshipResponse(internship), "SGK file is uploaded successfully");
    }




    private PageRequest getPageable(int page, int size, String sortBy, Sort.Direction order) {
        if (sortBy == null || order == null) {
            return PageRequest.of(page, size);
        }
        return PageRequest.of(page, size, Sort.by(order, sortBy));
    }
}
