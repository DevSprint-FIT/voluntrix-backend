package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.CreateReactionDTO;
import com.DevSprint.voluntrix_backend.dtos.ReactionDTO;
import com.DevSprint.voluntrix_backend.entities.ReactionEntity;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;

import java.time.LocalDateTime;

public class ReactionConverter {

    public static ReactionDTO toDTO(ReactionEntity entity){
        ReactionDTO dto = new ReactionDTO();
        dto.setId(entity.getId());
        dto.setSocialFeedId(entity.getSocialFeed().getId());
        dto.setUserId(entity.getUserId());
        dto.setUserType(entity.getUserType().name());
        dto.setReacted(entity.isReacted());
        dto.setCreatedAt(entity.getCreatedAt().toString());
        return dto;
    }
    public static ReactionEntity toEntity(CreateReactionDTO dto, SocialFeedEntity feed) {
        ReactionEntity entity = new ReactionEntity();
        entity.setSocialFeed(feed);
        entity.setUserId(dto.getUserId());
        entity.setUserType(UserType.valueOf(dto.getUserType().toUpperCase()));
        entity.setReacted(true);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

}
