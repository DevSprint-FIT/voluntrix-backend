package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationDTO;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEventParticipationEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VolunteerEventParticipationDTOConvert {

    // Converts a VolunteerEventParticipationEntity to a VolunteerEventParticipationDTO
    public VolunteerEventParticipationDTO toParticipationDTO(VolunteerEventParticipationEntity participationEntity) {
        VolunteerEventParticipationDTO participationDTO = new VolunteerEventParticipationDTO();
        participationDTO.setParticipationId(participationEntity.getParticipationId());
        participationDTO.setVolunteerId(participationEntity.getVolunteer().getVolunteerId());
        participationDTO.setVolunteerUsername(participationEntity.getVolunteer().getUser().getUsername());
        participationDTO.setEventId(participationEntity.getEvent().getEventId());
        participationDTO.setAreaOfContribution(participationEntity.getAreaOfContribution());
        participationDTO.setEventRewardPoints(participationEntity.getEventRewardPoints());
        return participationDTO;
    }

    // Converts a list of VolunteerEventParticipationEntity to a list of VolunteerEventParticipationDTO
    public List<VolunteerEventParticipationDTO> toParticipationDTOList(List<VolunteerEventParticipationEntity> participationEntities) {
        return participationEntities.stream()
                .map(this::toParticipationDTO)
                .collect(Collectors.toList());
    }

    // Converts a VolunteerEventParticipationDTO to a VolunteerEventParticipationEntity
    public VolunteerEventParticipationEntity toParticipationEntity(VolunteerEventParticipationDTO participationDTO, VolunteerEntity volunteer, EventEntity event) {
        VolunteerEventParticipationEntity participationEntity = new VolunteerEventParticipationEntity();
        participationEntity.setParticipationId(participationDTO.getParticipationId());
        participationEntity.setVolunteer(volunteer);
        participationEntity.setEvent(event);
        participationEntity.setAreaOfContribution(participationDTO.getAreaOfContribution());
        participationEntity.setEventRewardPoints(participationDTO.getEventRewardPoints());
        return participationEntity;
    }

    // Converts a VolunteerEventParticipationCreateDTO to a VolunteerEventParticipationEntity
    public VolunteerEventParticipationEntity toParticipationEntity(VolunteerEventParticipationCreateDTO createDTO, VolunteerEntity volunteer, EventEntity event) {
        VolunteerEventParticipationEntity participationEntity = new VolunteerEventParticipationEntity();
        participationEntity.setVolunteer(volunteer);
        participationEntity.setEvent(event);
        participationEntity.setAreaOfContribution(createDTO.getAreaOfContribution());
        participationEntity.setEventRewardPoints(0);
        return participationEntity;
    }
}