package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;
import java.security.Timestamp;

import com.DevSprint.voluntrix_backend.entities.EventEntity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class SponsorshipPackageDto implements Serializable {
    
    
    private Long id;
    private EventEntity event;

    private String type;
    private double price;
    
    
    private String benefits;

    private boolean isAvailable;
    private Timestamp createdAt;

    
}
