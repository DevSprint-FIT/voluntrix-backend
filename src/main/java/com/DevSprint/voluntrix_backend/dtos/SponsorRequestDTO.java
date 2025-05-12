package com.DevSprint.voluntrix_backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorRequestDTO {
    private Long id;
    private String company;
    private boolean isVerified;
    private String jobTitle;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$",message="Mobile number must be digits and can optionally start with '+'")
    private String mobileNumber;
    private String name;

    @NotBlank(message="Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    
    private String website;
}
