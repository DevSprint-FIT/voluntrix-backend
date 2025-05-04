package com.DevSprint.voluntrix_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.enums.EventType;
import com.DevSprint.voluntrix_backend.enums.EventVisibility;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private LocalDate eventStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventEndDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime eventTime;

    private String eventImageUrl;

    @Builder.Default
    private Integer volunteerCount = 0;

    @Enumerated(EnumType.STRING)
    private EventType eventType; // ONLINE or ONSITE

    @Enumerated(EnumType.STRING)
    private EventVisibility eventVisibility; // PRIVATE or PUBLIC

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus; // DRAFT, PENDING, ACTIVE, COMPLETE, DENIED

    private Boolean sponsorshipEnabled;
    private Boolean donationEnabled;

    @ManyToMany
    @JoinTable(name = "event_category", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CategoryEntity> categories;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_host_id", nullable = false)
    private VolunteerEntity eventHost;

    @OneToMany(mappedBy = "event")
    private Set<EventApplicationEntity> applications;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = true)
    private OrganizationEntity organization;
}