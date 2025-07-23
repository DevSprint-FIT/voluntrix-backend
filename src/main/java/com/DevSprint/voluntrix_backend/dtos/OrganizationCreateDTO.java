package com.DevSprint.voluntrix_backend.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationCreateDTO {

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phone;

    private String institute;

    private String bankName;

    private String accountNumber;

    private String description;

    private String website;

    private String facebookLink;

    private String linkedinLink;

    private String instagramLink;

}
