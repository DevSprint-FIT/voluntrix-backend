package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.VolunteerEventParticipationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
