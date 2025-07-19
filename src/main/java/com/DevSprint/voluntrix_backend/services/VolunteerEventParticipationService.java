package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerActiveEventDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCompletedEventDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerAppliedEventDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerEventStatsDTO;
import com.DevSprint.voluntrix_backend.dtos.EventLeaderboardDTO;
import com.DevSprint.voluntrix_backend.entities.VolunteerEventParticipationEntity;
import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.repositories.VolunteerEventParticipationRepository;
import com.DevSprint.voluntrix_backend.repositories.EventApplicationRepository;
import com.DevSprint.voluntrix_backend.utils.VolunteerEventParticipationDTOConvert;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VolunteerEventParticipationService {

    private final VolunteerEventParticipationRepository participationRepository;
    private final VolunteerEventParticipationDTOConvert participationDTOConvert;
    private final EventApplicationRepository eventApplicationRepository;

    // Create a new volunteer event participation record
    public VolunteerEventParticipationDTO createParticipation(VolunteerEventParticipationCreateDTO createDTO, VolunteerEntity volunteer, EventEntity event) {
        
        if (volunteer == null) {
            throw new IllegalArgumentException("Volunteer not found for ID: " + createDTO.getVolunteerId());
        }
    
        if (event == null) {
            throw new IllegalArgumentException("Event not found for ID: " + createDTO.getEventId());
        }

        // Prevent duplicate participation of a volunteer in the same event
        VolunteerEventParticipationEntity existingParticipation = participationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteer.getVolunteerId(), event.getEventId());

        if (existingParticipation != null) {
            throw new IllegalArgumentException("Volunteer is already participating in this event.");
        }

        VolunteerEventParticipationEntity participationEntity = participationDTOConvert.toParticipationEntity(createDTO, volunteer, event);

        // Initialize eventRewardPoints to 0 for new participation
        participationEntity.setEventRewardPoints(0);

        VolunteerEventParticipationEntity savedEntity = participationRepository.save(participationEntity);
        
        return participationDTOConvert.toParticipationDTO(savedEntity);
    }

    // Get all participation records for a specific volunteer
    public List<VolunteerEventParticipationDTO> getParticipationsByVolunteer(Long volunteerId) {
        List<VolunteerEventParticipationEntity> participationEntities = participationRepository.findByVolunteer_VolunteerId(volunteerId);
        
        if (participationEntities.isEmpty()) {
            throw new IllegalArgumentException("No participation records found for Volunteer ID: " + volunteerId);
        }

        return participationDTOConvert.toParticipationDTOList(participationEntities);
    }

    // Get all participation records for a specific event
    public List<VolunteerEventParticipationDTO> getParticipationsByEvent(Long eventId) {
        List<VolunteerEventParticipationEntity> participationEntities = participationRepository.findByEvent_EventId(eventId);

        if (participationEntities.isEmpty()) {
            throw new IllegalArgumentException("No participation records found for Event ID: " + eventId);
        }

        return participationDTOConvert.toParticipationDTOList(participationEntities);
    }

    // Get all participation records for a specific event where volunteers are available
    public List<VolunteerEventParticipationDTO> getAvailableParticipationsByEvent(Long eventId) {
        List<VolunteerEventParticipationEntity> participationEntities = participationRepository.findByEvent_EventIdAndVolunteer_IsAvailable(eventId, true);

        return participationDTOConvert.toParticipationDTOList(participationEntities);
    }

    // Get a specific participation record for a given volunteer and event
    public VolunteerEventParticipationDTO getParticipationByVolunteerAndEvent(Long volunteerId, Long eventId) {
        VolunteerEventParticipationEntity participationEntity = participationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteerId, eventId);
        if (participationEntity != null) {
            return participationDTOConvert.toParticipationDTO(participationEntity);
        } else {
            throw new IllegalArgumentException("Participation record not found for Volunteer ID: " + volunteerId + " and Event ID: " + eventId);
        }
    }

    // Get statistics for a specific volunteer's event participation
    public VolunteerEventStatsDTO getVolunteerEventStats(Long volunteerId) {
        long activeCount = participationRepository.countByVolunteerIdAndEventStatus(volunteerId, EventStatus.ACTIVE);
        long completedCount = participationRepository.countByVolunteerIdAndEventStatus(volunteerId, EventStatus.COMPLETE);
        long appliedCount = eventApplicationRepository.countPendingApplicationsByVolunteerId(volunteerId);

        return new VolunteerEventStatsDTO(activeCount, completedCount, appliedCount);
    }

    // Get all active event details for a specific volunteer
    public List<VolunteerActiveEventDTO> getActiveEventsByVolunteerId(Long volunteerId) {
        return participationRepository.findActiveEventsByVolunteerId(volunteerId);
    }
    
    // Get all completed event details for a specific volunteer
    public List<VolunteerCompletedEventDTO> getCompletedEventsByVolunteerId(Long volunteerId) {
        return participationRepository.findCompletedEventsByVolunteerId(volunteerId);
    }

    // Get all events that a volunteer has applied for
    public List<VolunteerAppliedEventDTO> getAppliedEventsByVolunteerId(Long volunteerId) {
        return eventApplicationRepository.findAppliedEventsByVolunteerId(volunteerId);
    }

    // Delete a specific participation record for a given volunteer and event
    public void deleteParticipationByVolunteerAndEvent(Long volunteerId, Long eventId) {
        VolunteerEventParticipationEntity participationEntity = participationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteerId, eventId);
    
        if (participationEntity != null) {
            participationRepository.delete(participationEntity);
        } else {
            throw new IllegalArgumentException("Participation record not found for Volunteer ID: " + volunteerId + " and Event ID: " + eventId);
        }
    }

    // Get event reward points for a specific volunteer and event
    public Integer getEventRewardPoints(Long volunteerId, Long eventId) {
        VolunteerEventParticipationEntity participation = participationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteerId, eventId);
        if (participation != null) {
            return participation.getEventRewardPoints() != null ? participation.getEventRewardPoints() : 0;
        }
        return 0;
    }

    // Update event reward points for a specific volunteer and event
    public void updateEventRewardPoints(Long volunteerId, Long eventId, Integer points) {
        VolunteerEventParticipationEntity participation = participationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteerId, eventId);
        if (participation != null) {
            participation.setEventRewardPoints(points);
            participationRepository.save(participation);
        }
    }

    // Add points to existing event reward points for a specific volunteer and event
    public void addEventRewardPoints(Long volunteerId, Long eventId, Integer pointsToAdd) {
        VolunteerEventParticipationEntity participation = participationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteerId, eventId);
        if (participation != null) {
            Integer currentPoints = participation.getEventRewardPoints();
            if (currentPoints == null) {
                currentPoints = 0;
            }
            participation.setEventRewardPoints(currentPoints + pointsToAdd);
            participationRepository.save(participation);
        }
    }

    // Get event leaderboard for a specific event
    public List<EventLeaderboardDTO> getEventLeaderboard(Long eventId) {
        List<EventLeaderboardDTO> leaderboard = participationRepository.findEventLeaderboard(eventId);

        return leaderboard;
    }
}