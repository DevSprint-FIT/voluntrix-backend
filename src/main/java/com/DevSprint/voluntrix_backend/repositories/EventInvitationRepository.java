package com.DevSprint.voluntrix_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.EventInvitationEntity;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;

public interface EventInvitationRepository extends JpaRepository<EventInvitationEntity, Long> {

        boolean existsByEventAndOrganization(EventEntity event, OrganizationEntity organization);

        @Query("SELECT ei FROM EventInvitationEntity ei WHERE ei.event = :event AND ei.organization = :organization")
        Optional<EventInvitationEntity> findByEventAndOrganization(@Param("event") EventEntity event,
                        @Param("organization") OrganizationEntity organization);

        boolean existsByEvent(EventEntity event);

        // // Find the count of applied events by volunteer ID
        // @Query("SELECT COUNT(ea) FROM EventApplicationEntity ea WHERE ea.volunteer.id
        // = :volunteerId AND ea.applicationStatus = 'PENDING'")
        // long countPendingApplicationsByVolunteerId(@Param("volunteerId") Long
        // volunteerId);

        // // Find all events that a volunteer has applied for
        // @Query("SELECT new
        // com.DevSprint.voluntrix_backend.dtos.VolunteerAppliedEventDTO(" +
        // "e.eventTitle, e.eventType, ea.contributionArea) " +
        // "FROM EventApplicationEntity ea " +
        // "JOIN ea.event e " +
        // "WHERE ea.volunteer.id = :volunteerId AND ea.applicationStatus = 'PENDING'")
        // List<VolunteerAppliedEventDTO>
        // findAppliedEventsByVolunteerId(@Param("volunteerId") Long volunteerId);

        List<EventInvitationEntity> findByOrganization(OrganizationEntity organization);
}
