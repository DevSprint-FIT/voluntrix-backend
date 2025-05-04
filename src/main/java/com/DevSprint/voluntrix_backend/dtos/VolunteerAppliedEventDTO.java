package com.DevSprint.voluntrix_backend.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.DevSprint.voluntrix_backend.enums.ContributionArea;
import com.DevSprint.voluntrix_backend.enums.EventType;

import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerAppliedEventDTO {
    private String eventName;
    private EventType eventType;
    private ContributionArea contributionArea;
}
