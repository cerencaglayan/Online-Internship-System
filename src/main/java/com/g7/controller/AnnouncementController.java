package com.g7.controller;

import com.g7.model.Announcement;
import com.g7.model.Company;
import com.g7.payload.mapper.AnnouncementMapper;
import com.g7.payload.request.AnnouncementCreateRequest;
import com.g7.payload.request.AnnouncementUpdateRequest;
import com.g7.payload.response.AnnouncementResponse;
import com.g7.payload.response.Response;
import com.g7.repository.AnnouncementRepository;
import com.g7.service.EmailService;
import com.g7.service.UbysApi;
import com.g7.spec.AnnouncementSpec;
import com.g7.util.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/announcement")
@AllArgsConstructor
public class AnnouncementController {
    private final AnnouncementRepository announcementRepository;
    private final UserUtil userUtil;
    private final UbysApi ubysApi;
    private final AnnouncementMapper announcementMapper;
    private final EmailService emailService;

    @GetMapping("/{id}")
    public ResponseEntity<Response<AnnouncementResponse>> getAnnouncement(@PathVariable long id) {

        Optional<Announcement> maybeAnnouncement = announcementRepository.findById(id);
        return maybeAnnouncement
                .map(announcement -> Response.ok(announcementMapper
                        .toResponse(announcement), "The announcement:"))
                .orElseGet(() -> Response.badRequest("Not valid Id"));

    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<Response<AnnouncementResponse>> updateAnnouncement(@PathVariable long id,
                                                                             @RequestBody AnnouncementUpdateRequest request) {

        Company currentCompany = null;
        if (userUtil.isCurrentUserCompany()) {
            currentCompany = userUtil.getCurrentCompany();
        }

        Announcement announcement=null;
        Optional<Announcement> maybeAnnouncement = announcementRepository.findById(id);
        if (maybeAnnouncement.isPresent()){
            announcement = maybeAnnouncement.get();

            announcement.setCompany(currentCompany);
            announcement.setTitle(request.getTitle());
            announcement.setDetails(request.getDetails());
            announcement.setStartDate(request.getStartDate());
            announcement.setEndDate(request.getEndDate());
            announcement.setIsActive(true);

            announcementRepository.save(announcement);

            emailService.sendEmail(ubysApi.getStudents(), " The internship opportunity: "+announcement.getTitle() +" is now updated.",
                    currentCompany.getName() + " just updated the announcement details.  Check it out!");

            return Response.ok(announcementMapper.toResponse(announcement),
                    "Announcement updated successfully");
        }else{
            return  Response.badRequest("ID is not valid");
        }


    }
    @PostMapping("/create")
    public ResponseEntity<Response<AnnouncementResponse>> createAnnouncement(@RequestBody AnnouncementCreateRequest request) {
        Company currentCompany = userUtil.getCurrentCompany();
        Announcement announcement = new Announcement();
        announcement.setCompany(currentCompany);
        announcement.setTitle(request.getTitle());
        announcement.setDetails(request.getDetails());
        announcement.setStartDate(request.getStartDate());
        announcement.setEndDate(request.getEndDate());
        announcement.setIsActive(true);
        announcementRepository.save(announcement);


        emailService.sendEmail(ubysApi.getStudents(), "New Internship Oppurtunity",
                currentCompany.getName() + " just posted a new announcement.  Check it out!");

        return Response.ok(announcementMapper.toResponse(announcement),
                "Announcement created successfully");
    }

    @GetMapping
    public ResponseEntity<Response<Page<AnnouncementResponse>>> getAnnouncements(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        return Response.ok(announcementRepository.findAll(AnnouncementSpec.byActive(), PageRequest.of(page, size))
                .map(announcementMapper::toResponse), "Announcement list retrieved successfully");
    }

    
    @PostMapping("/make-private")
    public ResponseEntity<Response<AnnouncementResponse>> makeAnnouncementPrivate(@RequestParam Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).orElse(null);
        if (announcement == null) {
            return Response.badRequest("Announcement not found");
        }
        announcement.setIsActive(false);
        announcementRepository.save(announcement);
        return Response.ok(announcementMapper.toResponse(announcement), "Announcement made private successfully");
    }

 }
