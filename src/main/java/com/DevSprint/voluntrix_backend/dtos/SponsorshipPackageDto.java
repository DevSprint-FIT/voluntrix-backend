package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;

@Data
public class SponsorshipPackageDto {
    private Long eventId;
    private String type;
    private double price;
    private String benefits;
    private boolean isAvailable;
}
