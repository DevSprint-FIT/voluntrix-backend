package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Institute data transfer object")
public class InstituteDTO implements Serializable {
    
    @NotBlank(message = "Institute key is required")
    @Size(min = 2, max = 50, message = "Institute key must be between 2 and 50 characters")
    @Schema(description = "Unique institute key", example = "harvard", required = true)
    private String key;
    
    @NotBlank(message = "Institute name is required")
    @Size(min = 2, max = 100, message = "Institute name must be between 2 and 100 characters")
    @Schema(description = "Institute name", example = "Harvard University", required = true)
    private String name;
    
    @NotBlank(message = "Institute domain is required")
    @Schema(description = "Institute domain", example = "@harvard.edu", required = true)
    private String domain;
}
