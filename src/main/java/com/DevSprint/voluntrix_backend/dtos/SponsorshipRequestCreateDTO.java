package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorshipRequestCreateDTO implements Serializable {
    
    @NotNull(message = "Sponsor ID is required")
    private Long sponsorId;

    @NotNull(message = "Sponsorship ID is required")
    private Long sponsorshipId;
}
