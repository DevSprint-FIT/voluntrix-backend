package com.DevSprint.voluntrix_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.enums.EventType;
import com.DevSprint.voluntrix_backend.enums.EventVisibility;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String eventTitle;
    private String eventDescription;
    private String eventLocation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime eventTime;

    private String eventImageUrl;

    @Enumerated(EnumType.STRING)
    private EventType eventType; // ONLINE or ONSITE

    @Enumerated(EnumType.STRING)
    private EventVisibility eventVisibility; // PRIVATE or PUBLIC

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus; // DRAFT, PENDING, ACTIVE, COMPLETE, DENIED


    private Boolean sponsorshipEnabled;
    private Boolean donationEnabled;
}