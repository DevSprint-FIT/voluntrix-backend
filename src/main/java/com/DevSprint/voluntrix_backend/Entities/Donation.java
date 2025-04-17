package com.DevSprint.voluntrix_backend.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private String status; // e.g., SUCCESS, FAILED, PENDING

    private LocalDateTime timestamp;

    @ManyToOne
    private Donor donor;
}
