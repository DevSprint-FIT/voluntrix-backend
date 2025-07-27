package com.DevSprint.voluntrix_backend.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sponsor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SponsorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sponsorId;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private boolean verified = false;

    @Column(nullable = true)
    private String jobTitle;

    @Column(nullable = true, length = 10)
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Column(nullable = true)
    private String website;

    @Column(nullable = true)
    private String sponsorshipNote;

    @Column(nullable = true)
    private String documentUrl;

    @Column(nullable = true)
    private String linkedinProfile;

    @Column(nullable = true)
    private String address;

    @CreationTimestamp
    private LocalDateTime appliedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}