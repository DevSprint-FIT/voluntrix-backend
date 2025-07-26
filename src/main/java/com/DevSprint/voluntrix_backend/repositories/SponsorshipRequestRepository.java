package com.DevSprint.voluntrix_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevSprint.voluntrix_backend.entities.SponsorshipRequestEntity;

@Repository
public interface SponsorshipRequestRepository extends JpaRepository<SponsorshipRequestEntity, Long> {

    Optional<SponsorshipRequestEntity> findByRequestId(Long requestId);

    List<SponsorshipRequestEntity> findBySponsor_SponsorId(Long sponsorId);

    List<SponsorshipRequestEntity> findBySponsorship_Event_EventId(Long eventId);

}
