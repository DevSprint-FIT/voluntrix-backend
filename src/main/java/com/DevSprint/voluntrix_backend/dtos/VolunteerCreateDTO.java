package com.DevSprint.voluntrix_backend.dtos;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String institute;

    @Size(max = 255, message = "About section can have at most 255 characters")
    private String about;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(?:\\+94|0)?7\\d{8}$", message = "Phone number must be a valid Sri Lankan mobile number")
    private String phoneNumber;
}