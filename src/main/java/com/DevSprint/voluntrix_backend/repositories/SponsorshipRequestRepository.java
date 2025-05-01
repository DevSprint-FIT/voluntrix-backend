package com.DevSprint.voluntrix_backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.DevSprint.voluntrix_backend.entities.SponsorshipRequest;

public interface SponsorshipRequestRepository extends JpaRepository<SponsorshipRequest, Long> {
   
}
