package com.DevSprint.voluntrix_backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstituteVerificationRequestDTO {
    
    @NotBlank(message = "Institute name is required")
    private String institute;
    
    @NotBlank(message = "Institute email is required")
    @Email(message = "Institute email should be valid")
    private String instituteEmail;
}