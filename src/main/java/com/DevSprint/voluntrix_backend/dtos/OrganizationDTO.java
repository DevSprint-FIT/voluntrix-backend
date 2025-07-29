package com.DevSprint.voluntrix_backend.dtos;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
    private Long id;
    private String name;
    private String username;
    private String institute;
    private String email;
    private String phone;
    private String accountNumber;
    private Boolean isVerified;
    private Integer followerCount;
    private LocalDateTime joinedDate;
    private String description;
    private String website;
    private String bankName;
    private String imageUrl;
    private String documentUrl;
    private String facebookLink;
    private String linkedinLink;
    private String instagramLink;

}
