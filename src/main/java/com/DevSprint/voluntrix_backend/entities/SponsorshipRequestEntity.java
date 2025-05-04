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
public class SponsorshipRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sponsor_id",nullable = false)
    private SponsorEntity sponsor;

    @ManyToOne
    @JoinColumn(name="sponsorship_package_id",nullable = false)
    private SponsorshipPackageEntity sponsorshipPackage;

    private Timestamp createdAt;

    
}
