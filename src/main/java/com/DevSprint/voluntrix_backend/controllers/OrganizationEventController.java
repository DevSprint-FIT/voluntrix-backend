package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.services.OrganizationEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/public/organizations/{organizationId}/events")
public class OrganizationEventController {

    @Autowired
    private OrganizationEventService organizationEventService;

    // Single endpoint to get events by status
    @GetMapping
    public ResponseEntity<List<EventDTO>> getEventsByStatus(
            @PathVariable Long organizationId,
            @RequestParam(required = false) EventStatus status) {

        List<EventDTO> events;

        if (status != null) {
            events = organizationEventService.getEventsByOrganizationAndStatus(organizationId, status);
        } else {
            events = organizationEventService.getAllEventsByOrganization(organizationId);
        }

        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    // status count API
    @GetMapping("/status-count")
    public ResponseEntity<Map<String, Long>> getEventStatusCount(@PathVariable Long organizationId) {
        Map<String, Long> statusCounts = organizationEventService.getEventStatusCounts(organizationId);
        return ResponseEntity.ok(statusCounts);
    }

}
