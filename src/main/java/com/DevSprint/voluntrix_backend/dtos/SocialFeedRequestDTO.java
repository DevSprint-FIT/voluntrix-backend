package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.MediaType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialFeedRequestDTO {
    private String content;
    private String mediaUrl;
    private MediaType mediaType;
    private Long mediaSizeInBytes;
}
