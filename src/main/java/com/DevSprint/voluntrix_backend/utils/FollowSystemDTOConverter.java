package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.FollowOrganizationDTO;
import com.DevSprint.voluntrix_backend.entities.FollowedOrganizationEntity;
import org.springframework.stereotype.Component;

@Component
public class FollowSystemDTOConverter {

    // FollowedOrganization to FollowOrganizationDTO
    public FollowOrganizationDTO toFollowOrganizationDTO(FollowedOrganizationEntity followedOrganization) {
        var dto = new FollowOrganizationDTO();
        followedOrganization.getVolunteerId();
        followedOrganization.getOrganizationId();

        dto.setVolunteerId(followedOrganization.getVolunteerId());
        dto.setOrganizationId(followedOrganization.getOrganizationId());
        return dto;
    }

    // FollowOrganizationDTO to FollowedOrganization
    public FollowedOrganizationEntity toFollowedOrganizationEntity(FollowOrganizationDTO dto) {
        var entity = new FollowedOrganizationEntity();
        entity.setVolunteerId(dto.getVolunteerId());
        entity.setOrganizationId(dto.getOrganizationId());
        return entity;
    }
}
