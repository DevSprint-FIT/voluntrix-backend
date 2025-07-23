package com.DevSprint.voluntrix_backend.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SponsorUpdateDTO {
    
    @Size(max = 100, message = "Company name can have at most 100 characters")
    private String company;
    
    @Size(max = 100, message = "Job title can have at most 100 characters")
    private String jobTitle;
    
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;
    
    private String website;
    
}
