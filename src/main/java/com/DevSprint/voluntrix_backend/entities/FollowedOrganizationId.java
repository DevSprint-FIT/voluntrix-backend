package com.DevSprint.voluntrix_backend.entities;

import java.io.Serializable;
import lombok.Data;

@Data
public class FollowedOrganizationId implements Serializable {

    private Long volunteerId;
    private Long organizationId;

    public FollowedOrganizationId() {}

    public FollowedOrganizationId(Long volunteerId, Long organizationId) {
        this.volunteerId = volunteerId;
        this.organizationId = organizationId;
    }
}
