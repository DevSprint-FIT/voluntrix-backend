package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organization")
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String institute;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = true)
    private String accountNumber;

    @Column(nullable = false)
    private Boolean isVerified = false;

    @Column(nullable = false)
    private Integer followerCount = 0;

    @Column(nullable = true, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String website;

    @Column(nullable = true)
    private String bankName;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventEntity> events = new ArrayList<>();

    @Column(nullable = true)
    private String facebookLink;

    @Column(nullable = true)
    private String linkedinLink;

    @Column(nullable = true)
    private String instagramLink;

    @OneToMany(mappedBy = "organization")
    private List<EventInvitationEntity> invitations;
}
