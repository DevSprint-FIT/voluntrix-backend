package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ReactionStatusDTO {
    private Long userId;
    private boolean reacted;
}
