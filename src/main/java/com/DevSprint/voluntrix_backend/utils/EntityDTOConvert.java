package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.EventApplicationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventApplicationDTO;
import com.DevSprint.voluntrix_backend.dtos.EventCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.entities.EventApplicationEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.exceptions.CategoryNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityDTOConvert {

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    // Event Mapping

    // EventEntity to EventDTO
    public EventDTO toEventDTO(EventEntity eventEntity) {
        return modelMapper.map(eventEntity, EventDTO.class);
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

        return eventEntity;
    }

    // List<EventEntity> to List<EventDTO>
    public List<EventDTO> toEventDTOList(List<EventEntity> eventEntityList) {
        return eventEntityList.stream().map(entity -> modelMapper.map(entity, EventDTO.class))
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
}
