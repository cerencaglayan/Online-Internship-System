package com.g7.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Internship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentMail;

    @Enumerated(EnumType.STRING)
    private EInternshipStatus status;

    @ManyToOne
    private Company company;

    private LocalDateTime applicationDate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "sgk_document_id")
    private FileKey sgkDocument;

    @ManyToOne
    private FileKey letterDocument;

    @ManyToOne
    private FileKey applicationForm;

    @ManyToOne
    private Announcement announcement;

}
