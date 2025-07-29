package com.DevSprint.voluntrix_backend.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowOrganizationDTO {
    private Long volunteerId;
    private Long organizationId;
}

