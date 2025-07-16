package com.DevSprint.voluntrix_backend.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerDTO {
    private Long volunteerId;
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String institute;
    private String instituteEmail;
    private Boolean isAvailable;
    private Integer volunteerLevel; 
    private Integer rewardPoints; 
    private Boolean isEventHost;  
    private String joinedDate;
    private String about;
    private String phoneNumber;
    private String profilePictureUrl;
}
