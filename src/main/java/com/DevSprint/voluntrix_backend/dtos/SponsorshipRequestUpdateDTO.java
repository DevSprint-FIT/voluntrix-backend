package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import com.DevSprint.voluntrix_backend.enums.SponsorshipRequestStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorshipRequestUpdateDTO implements Serializable {
    
    @NotNull(message = "Status is required")
    private SponsorshipRequestStatus status;
}
