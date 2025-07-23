package com.DevSprint.voluntrix_backend.dtos;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUpdateDTO {
    
    @Size(max = 100, message = "Organization name can have at most 100 characters")
    private String name;

    @Size(max = 255, message = "Description can have at most 255 characters")
    private String description;
    
    private String website;
    
    @Size(max = 20, message = "Phone number can have at most 20 characters")
    private String phone;
    
    private String bankName;
    
    private String accountNumber;
    
    private String imageUrl;
    
    private String facebookLink;
    
    private String linkedinLink;
    
    private String instagramLink;
}
