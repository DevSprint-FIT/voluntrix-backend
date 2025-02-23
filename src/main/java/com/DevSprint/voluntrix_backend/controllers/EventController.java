package com.DevSprint.voluntrix_backend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.DevSprint.voluntrix_backend.Entities.Event;
import com.DevSprint.voluntrix_backend.services.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/events")
@SecurityRequirement(name = "bearerAuth")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getEvents(){
        return eventService.getAllEvents();
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }
}
