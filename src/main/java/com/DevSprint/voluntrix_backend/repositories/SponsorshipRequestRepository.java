package com.DevSprint.voluntrix_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.DevSprint.voluntrix_backend.entities.SponsorshipRequestEntity;
import com.DevSprint.voluntrix_backend.enums.SponsorshipRequestStatus;

@Repository
public interface SponsorshipRequestRepository extends JpaRepository<SponsorshipRequestEntity, Long> {

    Optional<SponsorshipRequestEntity> findByRequestId(Long requestId);

    List<SponsorshipRequestEntity> findBySponsor_SponsorId(Long sponsorId);

    List<SponsorshipRequestEntity> findBySponsorship_Event_EventId(Long eventId);

    @Query("""
        SELECT e.eventId, e.eventTitle, e.eventStartDate, s.type, s.price, sr.requestId
        FROM SponsorshipRequestEntity sr 
        JOIN sr.sponsorship s 
        JOIN s.event e 
        WHERE sr.sponsor.sponsorId = :sponsorId 
        AND sr.status = :status
        """)
    List<Object[]> findEventDetailsWithSponsorshipBySponsorIdAndStatus(
        @Param("sponsorId") Long sponsorId, 
        @Param("status") SponsorshipRequestStatus status
    );

    @Query("""
        SELECT e.eventId, e.eventTitle, s.type, s.price, s.benefits, sr.requestId
        FROM SponsorshipRequestEntity sr 
        JOIN sr.sponsorship s 
        JOIN s.event e 
        WHERE sr.requestId = :requestId 
        AND sr.sponsor.sponsorId = :sponsorId
        """)
    Optional<Object[]> findEventDetailsWithSponsorshipByRequestIdAndSponsorId(
        @Param("requestId") Long requestId,
        @Param("sponsorId") Long sponsorId
    );


}
