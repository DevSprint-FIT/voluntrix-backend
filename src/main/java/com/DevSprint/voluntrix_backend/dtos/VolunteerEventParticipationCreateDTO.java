package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.ContributionArea;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerEventParticipationCreateDTO {
    private Long volunteerId;
    private Long eventId;
    private ContributionArea areaOfContribution;
}
