package com.DevSprint.voluntrix_backend.repositories;

import java.util.List;

import com.DevSprint.voluntrix_backend.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.DevSprint.voluntrix_backend.dtos.EventNameDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    // JPQL Query for Completed and Active events only
    @Query("SELECT new com.DevSprint.voluntrix_backend.dtos.EventNameDTO(e.eventId, e.eventTitle) FROM EventEntity e WHERE e.eventStatus = com.DevSprint.voluntrix_backend.enums.EventStatus.COMPLETE OR e.eventStatus = com.DevSprint.voluntrix_backend.enums.EventStatus.ACTIVE")
    List<EventNameDTO> findAllEventIdAndTitle();

    List<EventEntity> findByEventTitleContainingIgnoreCase(String eventTitle);

    List<EventEntity> findByOrganizationIdAndEventStatus(Long organizationId, EventStatus status);

    List<EventEntity> findByOrganizationId(Long organizationId);

    Long countByOrganizationIdAndEventStatus(Long organizationId, EventStatus eventStatus);

    List<EventEntity> findByEventHost(VolunteerEntity eventHost);

    List<EventEntity> findTop3ByEventStatusOrderByEventStartDateDesc(EventStatus eventStatus);

    List<EventEntity> findByEventStatusOrderByEventStartDateDesc(EventStatus status);

    @Modifying
    @Query("UPDATE EventEntity e SET e.volunteerCount = e.volunteerCount + 1 WHERE e.eventId = :eventId")
    void incrementVolunteerCountById(@Param("eventId") Long eventId);

    @Query("SELECT COUNT(e) FROM EventEntity e WHERE e.eventHost.volunteerId = :eventHostId AND (e.eventStatus = com.DevSprint.voluntrix_backend.enums.EventStatus.ACTIVE OR e.eventStatus = com.DevSprint.voluntrix_backend.enums.EventStatus.COMPLETE)")
    Long countByEventHostIdWithActiveOrCompleteStatus(@Param("eventHostId") Long eventHostId);

    @Query("SELECT COALESCE(SUM(e.eventHostRewardPoints), 0) FROM EventEntity e WHERE e.eventHost.volunteerId = :eventHostId")
    Long sumEventHostRewardPointsByHostId(@Param("eventHostId") Long eventHostId);
}