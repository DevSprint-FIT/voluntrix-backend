package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sponsorship_package")
public class SponsorshipPackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    private String type;
    private double price;
    private String benefits;
    private boolean isAvailable;
    private LocalDateTime createdAt;
}
