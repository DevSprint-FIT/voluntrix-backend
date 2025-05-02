package com.DevSprint.voluntrix_backend.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorDTO {
    private Long id;
    private String company;
    private boolean isVerified;
    private String jobTitle;
    private String mobileNumber;
    private String name;
    private String email;
    private String website;
}
