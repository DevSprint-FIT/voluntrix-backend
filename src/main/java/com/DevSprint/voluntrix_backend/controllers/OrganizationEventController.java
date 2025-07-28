package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.services.OrganizationEventService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.UserType;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/organizations/events")
public class OrganizationEventController {

    private final OrganizationEventService organizationEventService;
    private final CurrentUserService currentUserService;

    // Single endpoint to get events by status
    @GetMapping
    @RequiresRole(UserType.ORGANIZATION)
    public ResponseEntity<List<EventDTO>> getEventsByStatus(
            @RequestParam(required = false) EventStatus status) {

        List<EventDTO> events;
        Long organizationId = currentUserService.getCurrentEntityId();

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
    @RequiresRole(UserType.ORGANIZATION)
    public ResponseEntity<Map<String, Long>> getEventStatusCount() {
        Long organizationId = currentUserService.getCurrentEntityId();
        Map<String, Long> statusCounts = organizationEventService.getEventStatusCounts(organizationId);
        return ResponseEntity.ok(statusCounts);
    }

}
