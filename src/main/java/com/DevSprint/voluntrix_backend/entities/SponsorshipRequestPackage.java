package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SponsorshipRequestPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private SponsorshipRequest request;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private SponsorshipPackage sponsorshipPackage;

    private int preferenceRank;

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean isSelected;

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    
}
