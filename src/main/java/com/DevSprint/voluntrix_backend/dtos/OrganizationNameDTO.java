package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrganizationNameDTO implements Serializable {

    private Long organizationId;
    private String organizationName;
    private String organizationLogoUrl;
}
