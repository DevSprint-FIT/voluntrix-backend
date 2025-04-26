package com.DevSprint.voluntrix_backend.repository;

import com.DevSprint.voluntrix_backend.model.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    // You can add custom queries here later if needed
}
