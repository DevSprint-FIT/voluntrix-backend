package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "volunteer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long volunteerId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;
 
    @Column
    private String institute;

    @ElementCollection
    private List<String> interestAreas;

    @Column(nullable = false)
    private String availabilityStatus;

    @ElementCollection
    private List<String> preferredOrganizations;

    @Column(nullable = false)
    private Integer volunteerLevel = 1; 

    @Column(nullable = false)
    private Integer rewardPoints = 0; 
}
