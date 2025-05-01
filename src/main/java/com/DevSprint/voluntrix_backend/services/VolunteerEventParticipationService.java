package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationDTO;
import com.DevSprint.voluntrix_backend.entities.VolunteerEventParticipationEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.repositories.VolunteerEventParticipationRepository;
import com.DevSprint.voluntrix_backend.utils.VolunteerEventParticipationDTOConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerEventParticipationService {

    private final VolunteerEventParticipationRepository participationRepository;
    private final VolunteerEventParticipationDTOConvert participationDTOConvert;

    // Create a new volunteer event participation record
    public VolunteerEventParticipationDTO createParticipation(VolunteerEventParticipationDTO participationDTO, VolunteerEntity volunteer, EventEntity event) {
        
        VolunteerEventParticipationEntity participationEntity = participationDTOConvert.toParticipationEntity(participationDTO, volunteer, event);
        VolunteerEventParticipationEntity savedEntity = participationRepository.save(participationEntity);
        
        return participationDTOConvert.toParticipationDTO(savedEntity);
    }

    // Get all participation records for a specific volunteer
    public List<VolunteerEventParticipationDTO> getParticipationsByVolunteer(Long volunteerId) {
        List<VolunteerEventParticipationEntity> participationEntities = participationRepository.findByVolunteer_VolunteerId(volunteerId);
        return participationDTOConvert.toParticipationDTOList(participationEntities);
    }

    // Get all participation records for a specific event
    public List<VolunteerEventParticipationDTO> getParticipationsByEvent(Long eventId) {
        List<VolunteerEventParticipationEntity> participationEntities = participationRepository.findByEvent_EventId(eventId);
        return participationDTOConvert.toParticipationDTOList(participationEntities);
    }

    // Get a specific participation record for a given volunteer and event
    public VolunteerEventParticipationDTO getParticipationByVolunteerAndEvent(Long volunteerId, Long eventId) {
        VolunteerEventParticipationEntity participationEntity = participationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteerId, eventId);
        return participationDTOConvert.toParticipationDTO(participationEntity);
    }

    // Delete a specific participation record for a given volunteer and event
    public void deleteParticipationByVolunteerAndEvent(Long volunteerId, Long eventId) {
        VolunteerEventParticipationEntity participationEntity =
                participationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteerId, eventId);
    
        if (participationEntity != null) {
            participationRepository.delete(participationEntity);
        } else {
            throw new IllegalArgumentException("Participation record not found for Volunteer ID: " + volunteerId + " and Event ID: " + eventId);
        }
    } 
}
