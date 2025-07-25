package com.DevSprint.voluntrix_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipEntity;

@Repository
public interface SponsorshipRepository extends JpaRepository<SponsorshipEntity, Long> {

    List<SponsorshipEntity> findByEvent(EventEntity event);
}
