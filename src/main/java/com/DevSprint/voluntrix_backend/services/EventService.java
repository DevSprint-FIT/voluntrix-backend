package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;

public interface EventService {
    void addEvent(EventDTO eventDTO);

    void deleteEvent(Long eventId);
}
