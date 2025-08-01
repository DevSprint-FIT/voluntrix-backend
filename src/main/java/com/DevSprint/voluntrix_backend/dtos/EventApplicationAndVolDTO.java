package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import com.DevSprint.voluntrix_backend.enums.ApplicationStatus;
import com.DevSprint.voluntrix_backend.enums.ContributionArea;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventApplicationAndVolDTO implements Serializable {
    private Long id;
    private Long eventId;
    private Long volunteerId;
    private String volunteerName;
    private String description;
    private ContributionArea contributionArea;
    private ApplicationStatus applicationStatus;
}

