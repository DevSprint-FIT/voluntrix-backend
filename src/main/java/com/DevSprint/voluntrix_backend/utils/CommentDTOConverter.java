package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.CommentDTO;
import com.DevSprint.voluntrix_backend.dtos.CreateCommentDTO;
import com.DevSprint.voluntrix_backend.entities.CommentEntity;
import com.DevSprint.voluntrix_backend.entities.SocialFeedEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CommentDTOConverter {

    public CommentDTO toDTO(CommentEntity entity, String commenterName) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setSocialFeedId(entity.getSocialFeed().getId());
        dto.setUserUsername(entity.getUserUsername());
        dto.setUserType(entity.getUserType().name());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
        dto.setCommenterName(commenterName);
       // dto.setProfileImageUrl(profileImageUrl);
        return dto;
    }

    public CommentEntity toEntity(CreateCommentDTO dto, SocialFeedEntity feed) {
        CommentEntity entity = new CommentEntity();
        entity.setSocialFeed(feed);
        entity.setUserUsername(dto.getUserUsername());
        entity.setUserType(UserType.valueOf(dto.getUserType().toUpperCase()));
        entity.setContent(dto.getContent());
        entity.setCreatedAt(java.time.LocalDateTime.now());
        return entity;
    }
}
