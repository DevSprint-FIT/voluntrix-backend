package com.DevSprint.voluntrix_backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerCreateDTO {
    private String institute;
    
    @Email(message = "Institute email should be valid")
    private String instituteEmail;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;

    @Size(max = 255, message = "About section can have at most 255 characters")
    private String about;

    @Size(max = 2048, message = "Profile picture URL can have at most 2048 characters")
    private String profilePictureUrl;

    @Pattern(regexp = "^(?:\\+94|0)?7\\d{8}$", message = "Phone number must be a valid Sri Lankan mobile number")
    private String phoneNumber;
}