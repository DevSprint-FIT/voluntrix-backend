package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sponsors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private boolean isVerified;
    private String jobTitle;
    private String mobileNumber;
    private String name;
    private String email;
    private String website;
}
