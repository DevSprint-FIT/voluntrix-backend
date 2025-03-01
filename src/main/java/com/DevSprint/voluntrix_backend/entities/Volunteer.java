package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "volunteer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String skills;
    private String availability;
}
