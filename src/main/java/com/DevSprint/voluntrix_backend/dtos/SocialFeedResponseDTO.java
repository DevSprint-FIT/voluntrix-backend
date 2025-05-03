package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.MediaType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SocialFeedResponseDTO {
    private Long id;
    private String content;
    private String mediaUrl;
    private MediaType mediaType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String organizationName;
    private String organizationImageUrl;
    private int impressions;
    private int shares;
    private Long mediaSizeInBytes;
}
