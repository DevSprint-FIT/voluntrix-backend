package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.entities.Sponsor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestDto {
    
    private Long sponsorId;
    private Long eventId;


}