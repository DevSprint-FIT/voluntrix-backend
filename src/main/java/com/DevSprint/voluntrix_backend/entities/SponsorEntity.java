package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sponsor")
public class SponsorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sponsorId;
}
