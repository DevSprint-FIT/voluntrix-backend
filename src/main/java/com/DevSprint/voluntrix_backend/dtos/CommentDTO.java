package com.DevSprint.voluntrix_backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long socialFeedId;
    private String userUsername;
    private String userType;
    private String content;
    private String createdAt;
    private String commenterName;
    private String profileImageUrl;
}
