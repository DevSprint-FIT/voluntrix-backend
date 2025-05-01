package com.DevSprint.voluntrix_backend.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerEventParticipationDTO {
    private Long participationId;
    private Long volunteerId;
    private Long eventId;
    private String areaOfContribution;
}
