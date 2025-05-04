package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationCreateDTO;
import com.DevSprint.voluntrix_backend.entities.VolunteerEventParticipationEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.repositories.VolunteerEventParticipationRepository;
import com.DevSprint.voluntrix_backend.utils.VolunteerEventParticipationDTOConvert;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VolunteerEventParticipationService {

    private final VolunteerEventParticipationRepository participationRepository;
    private final VolunteerEventParticipationDTOConvert participationDTOConvert;

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

    // Get a specific participation record for a given volunteer and event
    public VolunteerEventParticipationDTO getParticipationByVolunteerAndEvent(Long volunteerId, Long eventId) {
        VolunteerEventParticipationEntity participationEntity = participationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteerId, eventId);
        if (participationEntity != null) {
            return participationDTOConvert.toParticipationDTO(participationEntity);
        } else {
            throw new IllegalArgumentException("Participation record not found for Volunteer ID: " + volunteerId + " and Event ID: " + eventId);
        }
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
}
