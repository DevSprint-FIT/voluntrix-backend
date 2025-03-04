package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor 
@AllArgsConstructor
@Data // Making getters and setters

public class SponsorDTO implements Serializable {
    
    private Long id;

    private String sponsorName;

   
}
