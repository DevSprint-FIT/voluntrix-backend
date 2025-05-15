package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import org.springframework.stereotype.Component;
import com.DevSprint.voluntrix_backend.dtos.EventRequestDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventEntityDTOConverter {

    // Convert EventEntity to EventRequestDTO
    public EventRequestDTO toEventRequestDTO(EventEntity event) {
        return new EventRequestDTO(
                event.getEventId(),
                event.getEventTitle(),
                event.getEventDate(),
                event.getEventStatus()
        );
    }

    // Convert a List of EventEntity to List of EventRequestDTO
    public List<EventRequestDTO> toEventRequestDTOList(List<EventEntity> events) {
        return events.stream()
                     .map(this::toEventRequestDTO)
                     .collect(Collectors.toList());
    }

    // Convert EventEntity to EventDTO
    public EventDTO toEventDTO(EventEntity eventEntity) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventId(eventEntity.getEventId());
        eventDTO.setEventTitle(eventEntity.getEventTitle());
        eventDTO.setEventDate(eventEntity.getEventDate());
        eventDTO.setEventLocation(eventEntity.getEventLocation());
        eventDTO.setEventStatus(eventEntity.getEventStatus()); // Use Enum directly
        return eventDTO;
    }


    // Convert a List of EventEntity to List of EventDTO
    public List<EventDTO> toEventDTOList(List<EventEntity> eventEntities) {
        return eventEntities.stream()
                .map(this::toEventDTO)  // Convert each EventEntity to EventDTO
                .collect(Collectors.toList());
    }
}
