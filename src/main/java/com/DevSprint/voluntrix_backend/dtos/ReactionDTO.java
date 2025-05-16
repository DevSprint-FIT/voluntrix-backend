package com.DevSprint.voluntrix_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDTO {
    private Long id;
    private Long socialFeedId;
    private Long userId;
    private String userType;
    private boolean reacted;
    private String createdAt;
}
