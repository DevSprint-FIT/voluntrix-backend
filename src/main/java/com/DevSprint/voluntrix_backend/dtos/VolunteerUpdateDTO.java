package com.DevSprint.voluntrix_backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerUpdateDTO {
    private String institute;

    @Email(message = "Institute email should be valid")
    private String instituteEmail;

    private Boolean isAvailable;

    private Boolean isEventHost;

    @Size(max = 255, message = "About section can have at most 255 characters")
    private String about;
}