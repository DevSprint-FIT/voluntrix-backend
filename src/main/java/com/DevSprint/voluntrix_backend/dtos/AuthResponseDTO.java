package com.DevSprint.voluntrix_backend.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    
    private String token;
    
    // User basic information
    private Long userId;
    private String email;
    private String username;
    private String fullName;
    
    // User status information
    private String role;
    private boolean isEmailVerified;
    private boolean isProfileCompleted;
    
    // Account metadata
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String authProvider;
    
    // Application state guidance for frontend
    private String nextStep;
    private String redirectUrl;
}
