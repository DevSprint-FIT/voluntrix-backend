package com.DevSprint.voluntrix_backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String accountNumber;
    private Boolean isVerified;
    private Integer followerCount;
}
