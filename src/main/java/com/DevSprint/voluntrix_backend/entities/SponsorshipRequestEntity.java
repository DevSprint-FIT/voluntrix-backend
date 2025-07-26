package com.DevSprint.voluntrix_backend.entities;

import com.DevSprint.voluntrix_backend.enums.SponsorshipRequestStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sponsorship_requests")
public class SponsorshipRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SponsorshipRequestStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsor_id", nullable = false)
    private SponsorEntity sponsor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsorship_id", nullable = false)
    private SponsorshipEntity sponsorship;
}