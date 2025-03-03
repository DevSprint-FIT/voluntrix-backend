package com.DevSprint.voluntrix_backend.services;

import java.time.LocalDate;
import java.util.List;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.enums.EventType;

public interface EventService {
    void addEvent(EventDTO eventDTO);

    void deleteEvent(Long eventId);

    EventDTO getEventById(Long eventId);

    List<EventDTO> getAllEvents();

    void updateEvent(Long eventId, EventDTO eventDTO);

    List<EventDTO> getFilterEvent(String eventLocation, LocalDate eventDate, EventType eventType);
}
