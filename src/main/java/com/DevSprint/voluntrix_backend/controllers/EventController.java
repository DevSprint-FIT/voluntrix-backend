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
import java.util.Map;
import java.util.HashMap;


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

    @GetMapping("/status-count/{organizationId}")
    public ResponseEntity<Map<String, Long>> getEventStatusCount(@PathVariable Long organizationId) {
        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("active", eventService.getActiveEventCount(organizationId));
        statusCounts.put("pending", eventService.getPendingEventCount(organizationId));
        statusCounts.put("completed", eventService.getCompletedEventCount(organizationId));

        return ResponseEntity.ok(statusCounts);
    }


}
