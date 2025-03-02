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
    private Long id;

    private String title;
    private String description;
    private String location;

    private LocalDate date;
    private LocalTime time;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private EventType type; // ONLINE or OFFLINE

    @Enumerated(EnumType.STRING)
    private EventVisibility visibility; // PRIVATE or PUBLIC

    @Enumerated(EnumType.STRING)
    private EventStatus status; // DRAFT, PENDING, ACTIVE, COMPLETE, DENIED

    private Boolean sponsorshipEnabled;
    private Boolean donationEnabled;
}
