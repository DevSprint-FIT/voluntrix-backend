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

    // @Query("""
    //     SELECT new com.DevSprint.voluntrix_backend.dtos.SponsorshipRequestEventDetailsDTO(
    //         e.eventId, e.eventTitle, e.startTime, s.packageType, s.price
    //     )
    //     FROM SponsorshipRequestEntity sr 
    //     JOIN sr.sponsorship s 
    //     JOIN s.event e 
    //     WHERE sr.sponsor.sponsorId = :sponsorId
    //     """)
    // List<SponsorshipRequestEventDetailsDTO> findAllEventDetailsForSponsor(
    //     @Param("sponsorId") Long sponsorId
    // );

}
