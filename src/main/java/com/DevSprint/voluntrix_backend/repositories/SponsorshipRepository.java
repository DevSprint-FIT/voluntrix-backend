package com.DevSprint.voluntrix_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipEntity;

@Repository
public interface SponsorshipRepository extends JpaRepository<SponsorshipEntity, Long> {
    
    List<SponsorshipEntity> findByEventEventId(Long eventId);
    
    List<SponsorshipEntity> findByIsAvailable(boolean isAvailable);
    
    List<SponsorshipEntity> findByEventAndIsAvailable(EventEntity event, boolean isAvailable);
    
    @Query("SELECT s FROM SponsorshipEntity s WHERE s.event.eventId = :eventId AND s.isAvailable = true")
    List<SponsorshipEntity> findAvailableSponsorshipsByEventId(@Param("eventId") Long eventId);
    
    Optional<SponsorshipEntity> findBySponsorshipId(Long sponsorshipId);
}
