package com.DevSprint.voluntrix_backend.services;

import java.util.List;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;

public interface EventService {
    void addEvent(EventDTO eventDTO);

    void deleteEvent(Long eventId);

    EventDTO getEventById(Long eventId);

    List<EventDTO> getAllEvents();
}
