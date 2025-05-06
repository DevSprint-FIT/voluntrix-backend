package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;

@Data

public class SponsorshipRequestDto {

    private long sponsorId;
    private long packageId;
    private String status;
    
}
