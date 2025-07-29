package com.DevSprint.voluntrix_backend.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SponsorDTO {
    private Long sponsorId;
    private String name; 
    private String email;
    private String company;
    private boolean verified;
    private String jobTitle;
    private String mobileNumber;
    private String website;
    private String sponsorshipNote;
    private String documentUrl;
    private String linkedinProfile;
    private String address;
    private LocalDateTime appliedAt;
}
