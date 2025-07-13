package com.DevSprint.voluntrix_backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDTO {
    private Long socialFeedId;
    private String userUsername;
    private String userType;
    private String content;
    private String profileImageUrl;
}
