package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SponsorshipCreateDTO implements Serializable {

    @NotBlank(message = "Sponsorship type is required")
    @Size(max = 100, message = "Sponsorship type must not exceed 100 characters")
    private String type;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    private Integer price;

    @Size(max = 1000, message = "Benefits must not exceed 1000 characters")
    private String benefits;

    @NotNull(message = "Event ID is required")
    private Long eventId;
}
