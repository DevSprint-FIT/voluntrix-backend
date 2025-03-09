package com.DevSprint.voluntrix_backend.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerDTO {
    private Long volunteerId;
    private String firstName;
    private String lastName;
    private String email;
    private String institute;
    private String availabilityStatus;
    private List<String> interestAreas;
    private List<String> preferredOrganizations;
    private Integer volunteerLevel; 
    private Integer rewardPoints;   
}
