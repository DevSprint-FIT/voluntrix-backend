package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.EventRequestDTO;
import com.DevSprint.voluntrix_backend.services.EventRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/event-requests")
public class EventRequestController {

    @Autowired
    private EventRequestService eventRequestService;

    @GetMapping("/{organizationId}")
    public ResponseEntity<List<EventRequestDTO>> getPendingEventRequests(@PathVariable Long organizationId) {
        List<EventRequestDTO> pendingEvents = eventRequestService.getPendingEventsByOrganization(organizationId);

        if (pendingEvents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pendingEvents); 

}
}
