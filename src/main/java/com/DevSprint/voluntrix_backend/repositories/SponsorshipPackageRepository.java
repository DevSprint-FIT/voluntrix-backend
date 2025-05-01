package com.DevSprint.voluntrix_backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.DevSprint.voluntrix_backend.entities.SponsorshipPackage;

public interface SponsorshipPackageRepository extends JpaRepository<SponsorshipPackage, Long> {
}
