package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.Entities.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonorRepository extends JpaRepository<Donor, Long> {
    Optional<Donor> findByEmail(String email);
}
