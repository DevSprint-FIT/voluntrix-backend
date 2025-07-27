package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.EventAndOrgDTO;
import com.DevSprint.voluntrix_backend.dtos.EventApplicationAndVolDTO;
import com.DevSprint.voluntrix_backend.dtos.EventApplicationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventApplicationDTO;
import com.DevSprint.voluntrix_backend.dtos.EventCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.dtos.EventInvitationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventInvitationDTO;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.entities.EventApplicationEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.EventInvitationEntity;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.exceptions.CategoryNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.OrganizationNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.CategoryRepository;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventDTOConverter {

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final OrganizationRepository organizationRepository;

    @PostConstruct
    public void configureModelMapper() {
        // Simply ignore ambiguous mappings
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

    // EventEntity to EventDTO
     public EventDTO toEventDTO(EventEntity eventEntity) {
        EventDTO eventDTO = modelMapper.map(eventEntity, EventDTO.class);
        
        // Manually set the correct eventHostRewardPoints to avoid ambiguity
        eventDTO.setEventHostRewardPoints(eventEntity.getEventHostRewardPoints());
        
        // Manually set eventHostId
        if (eventEntity.getEventHost() != null) {
            eventDTO.setEventHostId(eventEntity.getEventHost().getVolunteerId());
        }
        
        return eventDTO;
    }

    // EventCreateDTO to EventEntity
    public EventEntity toEventEntity(EventCreateDTO eventCreateDTO, VolunteerEntity eventHost) {
        EventEntity eventEntity = new EventEntity();

        eventEntity.setEventTitle(eventCreateDTO.getEventTitle());
        eventEntity.setEventDescription(eventCreateDTO.getEventDescription());
        eventEntity.setEventLocation(eventCreateDTO.getEventLocation());
        eventEntity.setEventStartDate(eventCreateDTO.getEventStartDate());
        eventEntity.setEventEndDate(eventCreateDTO.getEventEndDate());
        eventEntity.setEventTime(eventCreateDTO.getEventTime());
        eventEntity.setEventImageUrl(eventCreateDTO.getEventImageUrl());
        eventEntity.setEventType(eventCreateDTO.getEventType());
        eventEntity.setEventVisibility(eventCreateDTO.getEventVisibility());
        eventEntity.setEventStatus(eventCreateDTO.getEventStatus());
        eventEntity.setSponsorshipEnabled(eventCreateDTO.getSponsorshipEnabled());
        eventEntity.setDonationEnabled(eventCreateDTO.getDonationEnabled());
        eventEntity.setSponsorshipProposalUrl(eventCreateDTO.getSponsorshipProposalUrl());
        eventEntity.setDonationGoal(eventCreateDTO.getDonationGoal());

        // Set categories
        if (eventCreateDTO.getCategories() != null) {
            Set<CategoryEntity> categoryEntities = eventCreateDTO.getCategories().stream()
                    .map(dto -> categoryRepository.findById(dto.getCategoryId())
                            .orElseThrow(
                                    () -> new CategoryNotFoundException("Category not found: " + dto.getCategoryId())))
                    .collect(Collectors.toSet());
            eventEntity.setCategories(categoryEntities);
        }

        // Set event host
        eventEntity.setEventHost(eventHost);

        // Set organization
        if (eventCreateDTO.getOrganizationId() != null) {
            OrganizationEntity organization = organizationRepository.findById(eventCreateDTO.getOrganizationId())
                    .orElseThrow(() -> new OrganizationNotFoundException("Organization not found:"
                            + eventCreateDTO.getOrganizationId()));
            eventEntity.setOrganization(organization);
        }

        return eventEntity;
    }

    // EventDTO to EventEntity
    public EventEntity toEventEntity(EventDTO eventDTO) {
        EventEntity eventEntity = modelMapper.map(eventDTO, EventEntity.class);
        if (eventDTO.getCategories() != null) {
            Set<CategoryEntity> categoryEntities = eventDTO.getCategories().stream()
                    .map(dto -> categoryRepository.findById(dto.getCategoryId())
                            .orElseThrow(
                                    () -> new CategoryNotFoundException("Category not found: " + dto.getCategoryId())))
                    .collect(Collectors.toSet());
            eventEntity.setCategories(categoryEntities);
        }
        return eventEntity;
    }

    // List<EventEntity> to List<EventDTO>
    public List<EventDTO> toEventDTOList(List<EventEntity> eventEntityList) {
        return eventEntityList.stream().map(entity -> modelMapper.map(entity, EventDTO.class))
                .collect(Collectors.toList());
    }

    // List<EventEntity> to List<EventAndOrgDTO>
    public EventAndOrgDTO toEventAndOrgDTO(EventEntity entity) {
        EventAndOrgDTO dto = modelMapper.map(entity, EventAndOrgDTO.class);

        if (entity.getOrganization() != null) {
            dto.setOrganizationName(entity.getOrganization().getName());
            dto.setOrganizationImageUrl(entity.getOrganization().getImageUrl());
        }

        return dto;
    }

    // List<EventEntity> to List<EventAndOrgDTO>
    public List<EventAndOrgDTO> toEventAndOrgDTOList(List<EventEntity> entities) {
        return entities.stream()
                .map(this::toEventAndOrgDTO)
                .collect(Collectors.toList());
    }

    // Event Application Mapping

    // EventApplicationEntity to EventApplicationDTO
    public EventApplicationDTO toEventApplicationDTO(EventApplicationEntity eventApplicationEntity) {
        return modelMapper.map(eventApplicationEntity, EventApplicationDTO.class);
    }

    // EventApplicationCreateDTO to EventApplicationEntity
    public EventApplicationEntity toEventApplicationEntity(EventApplicationCreateDTO eventApplicationCreateDTO,
            EventEntity eventEntity, VolunteerEntity volunteerEntity) {
        EventApplicationEntity eventApplicationEntity = new EventApplicationEntity();

        eventApplicationEntity.setDescription(eventApplicationCreateDTO.getDescription());
        eventApplicationEntity.setContributionArea(eventApplicationCreateDTO.getContributionArea());
        eventApplicationEntity.setApplicationStatus(eventApplicationCreateDTO.getApplicationStatus());

        // Set event
        eventApplicationEntity.setEvent(eventEntity);

        // set volunteer
        eventApplicationEntity.setVolunteer(volunteerEntity);

        return eventApplicationEntity;
    }

    // List<EventApplicationEntity> to List<EventApplicationDTO>
    public List<EventApplicationDTO> toEventApplicationDTOList(
            List<EventApplicationEntity> eventEntityApplicationList) {
        return eventEntityApplicationList.stream().map(entity -> modelMapper.map(entity, EventApplicationDTO.class))
                .collect(Collectors.toList());
    }

    // EventApplicationEntity to EventApplicationAndVolDTO
    public EventApplicationAndVolDTO toEventApplicationAndVolDTO(EventApplicationEntity entity) {
        EventApplicationAndVolDTO dto = new EventApplicationAndVolDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setContributionArea(entity.getContributionArea());
        dto.setApplicationStatus(entity.getApplicationStatus());

        if (entity.getEvent() != null) {
            dto.setEventId(entity.getEvent().getEventId());
        }

        if (entity.getVolunteer() != null) {
            dto.setVolunteerId(entity.getVolunteer().getVolunteerId());
            dto.setVolunteerName(entity.getVolunteer().getFirstName() + " " + entity.getVolunteer().getLastName());
        }

        return dto;
    }

    // List<EventApplicationEntity> to List<EventApplicationAndVolDTO
    public List<EventApplicationAndVolDTO> toEventApplicationAndVolDTOList(List<EventApplicationEntity> entities) {
        return entities.stream()
                .map(this::toEventApplicationAndVolDTO)
                .collect(Collectors.toList());
    }

    // Event Invitation Mapping

    // EventInvitationEntity to EventInvitationDTO
    public EventInvitationDTO toEventInvitationDTO(EventInvitationEntity eventInvitationEntity) {
        return modelMapper.map(eventInvitationEntity, EventInvitationDTO.class);
    }

    // EventInvitationCreateDTO to EventInvitationEntity
    public EventInvitationEntity toEventInvitationEntity(EventInvitationCreateDTO eventInvitationCreateDTO,
            EventEntity eventEntity, OrganizationEntity organizationEntity) {
        EventInvitationEntity eventInvitationEntity = new EventInvitationEntity();

        eventInvitationEntity.setApplicationStatus(eventInvitationCreateDTO.getApplicationStatus());

        // Set event
        eventInvitationEntity.setEvent(eventEntity);

        // Set organization
        eventInvitationEntity.setOrganization(organizationEntity);

        return eventInvitationEntity;
    }

    // List<EventInvitationEntity> to List<EventInvitationDTO>
    public List<EventInvitationDTO> toEventInvitationDTOList(
            List<EventInvitationEntity> eventInvitationEntityList) {
        return eventInvitationEntityList.stream()
                .map(entity -> modelMapper.map(entity, EventInvitationDTO.class))
                .collect(Collectors.toList());
    }
}
