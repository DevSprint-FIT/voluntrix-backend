package com.DevSprint.voluntrix_backend.entities;

import java.security.Timestamp;




import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;

@Entity
public class SponsorshipPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    private String type;
    private double price;
    
    
    private String benefits;

    private boolean isAvailable;
    private Timestamp createdAt;

    
}
