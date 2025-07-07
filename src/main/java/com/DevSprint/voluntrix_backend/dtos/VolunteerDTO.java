package com.DevSprint.voluntrix_backend.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerDTO {
    private Long volunteerId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String institute;
    private Boolean isAvailable;
    private Integer volunteerLevel; 
    private Integer rewardPoints; 
    private Boolean isEventHost;  
    private LocalDate joinedDate;
    private String about;
    private String phoneNumber;
    private String profilePictureUrl;
}
