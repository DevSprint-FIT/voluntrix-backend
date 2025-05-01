package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.SocialFeedResponseDTO;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityDTOConverter {
    public SocialFeedResponseDTO toSocialFeedResponseDTO(SocialFeedEntity socialFeed){
        SocialFeedResponseDTO dto = new SocialFeedResponseDTO();
        dto.setId(socialFeed.getId());
        dto.setContent(socialFeed.getContent());
        dto.setMediaUrl(socialFeed.getMediaUrl());
        dto.setMediaType(socialFeed.getMediaType());
        dto.setCreatedAt(socialFeed.getCreatedAt());
        dto.setUpdatedAt(socialFeed.getUpdatedAt());
        dto.setOrganizationName(socialFeed.getOrganization().getName());
        dto.setOrganizationImageUrl(socialFeed.getOrganization().getImageUrl());
        return dto;

    }

    public List<SocialFeedResponseDTO> toSocialFeedResponseDTOList(List<SocialFeedEntity> socialFeeds){
        return socialFeeds.stream()
                .map(this::toSocialFeedResponseDTO)
                .collect(Collectors.toList());
    }
}
