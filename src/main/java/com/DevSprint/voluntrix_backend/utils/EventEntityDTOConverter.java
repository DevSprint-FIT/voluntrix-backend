package com.DevSprint.voluntrix_backend.utils;

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
}
