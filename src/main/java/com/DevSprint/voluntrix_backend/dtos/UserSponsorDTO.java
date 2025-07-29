package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSponsorDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String companyName;
    private String phoneNumber;
    private String bio;
    private Boolean isActive;
    private Boolean isOnline;
    private LocalDateTime createdAt;
    private LocalDateTime lastSeen;
    
    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
