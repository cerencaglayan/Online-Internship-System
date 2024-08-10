package com.g7.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementCreateRequest {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String details;
}
