package com.g7.payload.mapper;

import com.g7.model.Announcement;
import com.g7.payload.response.AnnouncementResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AnnouncementMapper {
    private final CompanyMapper companyMapper;

    public AnnouncementResponse toResponse(Announcement announcement) {

        return new AnnouncementResponse(
                announcement.getId(),
                announcement.getTitle(),
                announcement.getStartDate(),
                announcement.getEndDate(),
                announcement.getDetails(),
                companyMapper.toResponse(announcement.getCompany())
        );
    }


}
