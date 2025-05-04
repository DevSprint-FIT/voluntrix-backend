package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.entities.SponsorEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPackageDto {
    
    private Long sponsorId;
    private Long eventId;


}