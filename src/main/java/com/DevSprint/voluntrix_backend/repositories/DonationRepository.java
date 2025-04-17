package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.Entities.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
