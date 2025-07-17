package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "volunteer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long volunteerId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String institute;

    @Column(nullable = false)
    private Boolean isAvailable = false;

    @Column(nullable = false)
    private Integer volunteerLevel = 1;

    @Column(nullable = false)
    private Integer rewardPoints = 0;

    @Column(nullable = false)
    private Boolean isEventHost = false;

    @Column(nullable = false)
    private LocalDate joinedDate;

    @Column
    private String about;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(length = 2048)
    private String profilePictureUrl;

    @PrePersist
    protected void onCreate() {
        this.joinedDate = LocalDate.now();
    }

    @OneToMany(mappedBy = "eventHost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventEntity> hostedEvents = new ArrayList<>();

    @OneToMany(mappedBy = "volunteer")
    private Set<EventApplicationEntity> applications;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}

