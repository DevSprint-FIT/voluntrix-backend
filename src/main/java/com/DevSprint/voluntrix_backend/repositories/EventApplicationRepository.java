package com.DevSprint.voluntrix_backend.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.DevSprint.voluntrix_backend.dtos.VolunteerAppliedEventDTO;
import com.DevSprint.voluntrix_backend.entities.EventApplicationEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;

public interface EventApplicationRepository extends JpaRepository<EventApplicationEntity, Long> {

    boolean existsByEventAndVolunteer(EventEntity event, VolunteerEntity volunteer);

    @Query("SELECT ea FROM EventApplicationEntity ea WHERE ea.event = :event AND ea.volunteer = :volunteer")
    Optional<EventApplicationEntity> findByEventAndVolunteer(@Param("event") EventEntity event,
            @Param("volunteer") VolunteerEntity volunteer);

    // Find the count of applied events by volunteer ID
    @Query("SELECT COUNT(ea) FROM EventApplicationEntity ea WHERE ea.volunteer.id = :volunteerId AND ea.applicationStatus = 'PENDING'")
    long countPendingApplicationsByVolunteerId(@Param("volunteerId") Long volunteerId);

    // Find all events that a volunteer has applied for
   @Query("SELECT new com.DevSprint.voluntrix_backend.dtos.VolunteerAppliedEventDTO(" +
      "ea.id, e.eventTitle, e.eventType, ea.contributionArea) " +
      "FROM EventApplicationEntity ea " +
      "JOIN ea.event e " +
      "WHERE ea.volunteer.id = :volunteerId AND ea.applicationStatus = 'PENDING'")
   List<VolunteerAppliedEventDTO> findAppliedEventsByVolunteerId(@Param("volunteerId") Long volunteerId);

   List<EventApplicationEntity> findByEvent(EventEntity event);

   boolean existsByVolunteerAndEvent(VolunteerEntity volunteer, EventEntity event);
}
