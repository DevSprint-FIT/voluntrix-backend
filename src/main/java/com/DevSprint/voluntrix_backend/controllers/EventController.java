package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.models.Event;
import com.DevSprint.voluntrix_backend.services.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getEvents(){
        return eventService.getAllEvents();
    }
}
