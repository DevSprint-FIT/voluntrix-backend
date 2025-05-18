package com.DevSprint.voluntrix_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponseDto {
    private String token;
    private String role;
    private boolean isProfileCompleted;
}
