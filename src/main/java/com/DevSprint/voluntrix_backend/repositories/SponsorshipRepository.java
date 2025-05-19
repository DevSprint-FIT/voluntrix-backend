package com.DevSprint.voluntrix_backend.repositories;


import com.DevSprint.voluntrix_backend.entities.Sponsorship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Long> {
}
