package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.dtos.VolunteerActiveEventDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCompletedEventDTO;
import com.DevSprint.voluntrix_backend.dtos.EventLeaderboardDTO;
import com.DevSprint.voluntrix_backend.entities.VolunteerEventParticipationEntity;
import com.DevSprint.voluntrix_backend.enums.EventStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerEventParticipationRepository extends JpaRepository<VolunteerEventParticipationEntity, Long> {

   // Find all participation records for a given volunteer
   List<VolunteerEventParticipationEntity> findByVolunteer_VolunteerId(Long volunteerId);

   // Find all participation records for a given event
   List<VolunteerEventParticipationEntity> findByEvent_EventId(Long eventId);

   // Find participation record by both volunteer and event
   VolunteerEventParticipationEntity findByVolunteer_VolunteerIdAndEvent_EventId(Long volunteerId, Long eventId);

   // Find participation event count by event status
   @Query("SELECT COUNT(vp) FROM VolunteerEventParticipationEntity vp WHERE vp.volunteer.id = :volunteerId AND vp.event.eventStatus = :status")
   long countByVolunteerIdAndEventStatus(@Param("volunteerId") Long volunteerId, @Param("status") EventStatus status);

   // Find all active events for a given volunteer
   @Query("SELECT new com.DevSprint.voluntrix_backend.dtos.VolunteerActiveEventDTO(" +
      "e.eventTitle, e.eventStartDate, e.eventEndDate, e.eventLocation) " +
      "FROM VolunteerEventParticipationEntity vep " +"JOIN vep.event e " +
      "WHERE vep.volunteer.id = :volunteerId AND e.eventStatus = com.DevSprint.voluntrix_backend.enums.EventStatus.ACTIVE")
   List<VolunteerActiveEventDTO> findActiveEventsByVolunteerId(@Param("volunteerId") Long volunteerId);

   // Find all completed events for a given volunteer
   @Query("SELECT new com.DevSprint.voluntrix_backend.dtos.VolunteerCompletedEventDTO(" +
      "e.eventTitle, e.eventStartDate, e.eventEndDate, e.eventType, vep.areaOfContribution) " +
      "FROM VolunteerEventParticipationEntity vep " +
      "JOIN vep.event e " +
      "WHERE vep.volunteer.id = :volunteerId " +
      "AND e.eventStatus = com.DevSprint.voluntrix_backend.enums.EventStatus.COMPLETE")
   List<VolunteerCompletedEventDTO> findCompletedEventsByVolunteerId(@Param("volunteerId") Long volunteerId);

   // For event leaderboard - get volunteers ranked by event reward points for a specific event
   @Query("SELECT new com.DevSprint.voluntrix_backend.dtos.EventLeaderboardDTO(" +
           "v.firstName, v.lastName,vep.eventRewardPoints, v.profilePictureUrl) " +
           "FROM VolunteerEventParticipationEntity vep " +
           "JOIN vep.volunteer v " +
           "WHERE vep.event.eventId = :eventId " +
           "ORDER BY vep.eventRewardPoints DESC")
   List<EventLeaderboardDTO> findEventLeaderboard(@Param("eventId") Long eventId);
}
