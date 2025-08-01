package com.DevSprint.voluntrix_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequestDTO {
    
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
