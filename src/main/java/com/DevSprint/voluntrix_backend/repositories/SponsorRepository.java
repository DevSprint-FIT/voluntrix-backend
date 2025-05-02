package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorRepository extends JpaRepository<SponsorEntity, Long> {
}
