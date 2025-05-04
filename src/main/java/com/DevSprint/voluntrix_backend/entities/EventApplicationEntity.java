package com.DevSprint.voluntrix_backend.entities;

import com.DevSprint.voluntrix_backend.enums.ApplicationStatus;
import com.DevSprint.voluntrix_backend.enums.ContributionArea;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "event_application")
public class EventApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private VolunteerEntity volunteer;

    private String description;

    @Enumerated(EnumType.STRING)
    private ContributionArea contributionArea;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

}