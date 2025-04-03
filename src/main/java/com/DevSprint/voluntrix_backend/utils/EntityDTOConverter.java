package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.FollowOrganizationDTO;
import com.DevSprint.voluntrix_backend.entities.FollowedOrganization;
import org.springframework.stereotype.Component;

@Component
public class EntityDTOConverter {

    // FollowedOrganization to FollowOrganizationDTO
    public FollowOrganizationDTO toFollowOrganizationDTO(FollowedOrganization followedOrganization) {
        var dto = new FollowOrganizationDTO();
        dto.setId(followedOrganization.getId());
        dto.setVolunteerId(followedOrganization.getVolunteerId());
        dto.setOrganizationId(followedOrganization.getOrganizationId());
        return dto;
    }

    // FollowOrganizationDTO to FollowedOrganization
    public FollowedOrganization toFollowedOrganizationEntity(FollowOrganizationDTO dto) {
        var entity = new FollowedOrganization();
        entity.setVolunteerId(dto.getVolunteerId());
        entity.setOrganizationId(dto.getOrganizationId());
        return entity;
    }
}
