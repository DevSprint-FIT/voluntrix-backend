package com.DevSprint.voluntrix_backend.entities;


import java.security.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class SponsorshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sponsor_id",nullable = false)
    private Sponsor sponsor;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    private Timestamp createdAt;

    
}
