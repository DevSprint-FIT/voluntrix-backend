package com.DevSprint.voluntrix_backend.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
    private Long id;
    private String name;
    private String institute;
    private String email;
    private String phone;
    private String accountNumber;
    private Boolean isVerified;
    private Integer followerCount;
}
