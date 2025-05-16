package com.DevSprint.voluntrix_backend.entities;

import com.DevSprint.voluntrix_backend.enums.UserType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "social_feed_id", nullable = false)
    private SocialFeedEntity socialFeed;

    @Column(nullable = false)
    private Long userId; // This is either volunteerId, organizationId or sponsorId

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType; // Enum: Volunteer, Sponsor, Organization

    private boolean reacted = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
