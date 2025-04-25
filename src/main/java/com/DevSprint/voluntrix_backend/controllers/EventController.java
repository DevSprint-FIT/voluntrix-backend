package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/active/{organizationId}")
    public ResponseEntity<List<EventDTO>> getActiveEvents(@PathVariable Long organizationId){
        List<EventDTO> activeEvents = eventService.getActiveEventsByOrganization(organizationId);

        if(activeEvents.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activeEvents);
    }


}
