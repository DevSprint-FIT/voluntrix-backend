package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;

// Manual mapping
// Can be automated with ModelMapper
@Component
public class EntityDTOConvert {

    //Event Mapping

    // EventEntity to EventDTO
    public EventDTO toEventDTO(EventEntity eventEntity) {

        var eventDTO = new EventDTO();

        eventDTO.setEventId(eventEntity.getEventId());
        eventDTO.setEventTitle(eventEntity.getEventTitle());
        eventDTO.setEventDescription(eventEntity.getEventDescription());
        eventDTO.setEventLocation(eventEntity.getEventLocation());
        eventDTO.setEventDate(eventEntity.getEventDate());
        eventDTO.setEventTime(eventEntity.getEventTime());
        eventDTO.setEventImageUrl(eventEntity.getEventImageUrl());
        eventDTO.setEventType(eventEntity.getEventType());
        eventDTO.setEventVisibility(eventEntity.getEventVisibility());
        eventDTO.setEventStatus(eventEntity.getEventStatus());
        eventDTO.setSponsorshipEnabled(eventEntity.getSponsorshipEnabled());
        eventDTO.setDonationEnabled(eventEntity.getDonationEnabled());

        return eventDTO;
    }

    // EventDTO to EventEntity
    public EventEntity toEventEntity(EventDTO eventDTO) {

        var eventEntity = new EventEntity();

        eventEntity.setEventId(eventDTO.getEventId());
        eventEntity.setEventTitle(eventDTO.getEventTitle());
        eventEntity.setEventDescription(eventDTO.getEventDescription());
        eventEntity.setEventLocation(eventDTO.getEventLocation());
        eventEntity.setEventDate(eventDTO.getEventDate());
        eventEntity.setEventTime(eventDTO.getEventTime());
        eventEntity.setEventImageUrl(eventDTO.getEventImageUrl());
        eventEntity.setEventType(eventDTO.getEventType());
        eventEntity.setEventVisibility(eventDTO.getEventVisibility());
        eventEntity.setEventStatus(eventDTO.getEventStatus());
        eventEntity.setSponsorshipEnabled(eventDTO.getSponsorshipEnabled());
        eventEntity.setDonationEnabled(eventDTO.getDonationEnabled());

        return eventEntity;
    }

    // List<EventEntity> to List<EventDTO>
    public List<EventDTO> toEventDTOList(List<EventEntity> eventEntityList) {
        return eventEntityList.stream().map(entity -> {
            EventDTO eventDTO = new EventDTO();

            eventDTO.setEventId(entity.getEventId());
            eventDTO.setEventTitle(entity.getEventTitle());
            eventDTO.setEventDescription(entity.getEventDescription());
            eventDTO.setEventLocation(entity.getEventLocation());
            eventDTO.setEventDate(entity.getEventDate());
            eventDTO.setEventTime(entity.getEventTime());
            eventDTO.setEventImageUrl(entity.getEventImageUrl());
            eventDTO.setEventType(entity.getEventType());
            eventDTO.setEventVisibility(entity.getEventVisibility());
            eventDTO.setEventStatus(entity.getEventStatus());
            eventDTO.setSponsorshipEnabled(entity.getSponsorshipEnabled());
            eventDTO.setDonationEnabled(entity.getDonationEnabled());

            return eventDTO;
        }).collect(Collectors.toList());
    }
}
