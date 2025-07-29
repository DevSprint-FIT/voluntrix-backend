package com.DevSprint.voluntrix_backend.dtos;

import java.time.LocalDateTime;

import com.DevSprint.voluntrix_backend.enums.AuthProvider;
import com.DevSprint.voluntrix_backend.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentUserDTO {
    private Long userId;
    private String email;
    private String handle;
    private String fullName;
    private UserType role;
    private Boolean isEmailVerified;
    private Boolean isProfileCompleted;
    private AuthProvider authProvider;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    
    // Additional info for UI
    private String nextStep;
    private String redirectUrl;
    private Long entityId; // The role-specific entity ID (volunteerId, sponsorId, etc.)
}
