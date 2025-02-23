package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.models.Event;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }
}
