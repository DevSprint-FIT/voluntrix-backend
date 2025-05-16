package com.DevSprint.voluntrix_backend.entities;

import java.io.Serializable;
import lombok.Data;

@Data
public class FollowedOrganizationIdEntity implements Serializable {

    private Long volunteerId;
    private Long organizationId;

    public FollowedOrganizationIdEntity() {}

    public FollowedOrganizationIdEntity(Long volunteerId, Long organizationId) {
        this.volunteerId = volunteerId;
        this.organizationId = organizationId;
    }
}
