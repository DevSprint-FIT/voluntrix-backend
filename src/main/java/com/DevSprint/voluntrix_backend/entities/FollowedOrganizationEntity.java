package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "followed_organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FollowedOrganizationIdEntity.class)
public class FollowedOrganizationEntity {


    @Id
    @Column(nullable = false)
    private Long volunteerId;

    @Id
    @Column(nullable = false)
    private Long organizationId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime followedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteerId", insertable = false, updatable = false)
    private VolunteerEntity volunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId", insertable = false, updatable = false)
    private OrganizationEntity organization;







}
