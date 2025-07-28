package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.EventApplicationAndVolDTO;
import com.DevSprint.voluntrix_backend.dtos.EventApplicationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventApplicationDTO;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.EventApplicationService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/event-applications")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EventApplicationController {

    private final EventApplicationService eventApplicationService;
    private final CurrentUserService currentUserService;


    @GetMapping("/all")
    @RequiresRole({UserType.ADMIN, UserType.VOLUNTEER})
    public ResponseEntity<List<EventApplicationDTO>> getAllEventApplications() {
        return new ResponseEntity<List<EventApplicationDTO>>(eventApplicationService.getAllEventApplications(),
                HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<Void> addEventApplication(@RequestBody EventApplicationCreateDTO eventApplicationCreateDTO) {
        if (eventApplicationCreateDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long volunteerId = currentUserService.getCurrentEntityId();
        eventApplicationService.addEventApplication(eventApplicationCreateDTO, volunteerId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{eventApplicationId}/details")
    @RequiresRole({UserType.ADMIN, UserType.VOLUNTEER})
    public ResponseEntity<EventApplicationDTO> getEventApplicationById(@PathVariable Long eventApplicationId) {
        if (eventApplicationId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var selectedApplication = eventApplicationService.getEventApplicationById(eventApplicationId);
        return new ResponseEntity<>(selectedApplication, HttpStatus.OK);
    }

    @PatchMapping(value = "/{eventApplicationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequiresRole({UserType.VOLUNTEER, UserType.ADMIN})
    public ResponseEntity<Void> updateEventApplication(@PathVariable Long eventApplicationId,
            @Valid @RequestBody EventApplicationCreateDTO eventApplicationCreateDTO) {
        if (eventApplicationId == null || eventApplicationCreateDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        eventApplicationService.updateEventApplication(eventApplicationCreateDTO, eventApplicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{eventApplicationId}")
    @RequiresRole({UserType.VOLUNTEER, UserType.ADMIN})
    public ResponseEntity<Void> deleteEventApplication(@PathVariable Long eventApplicationId) {
        if (eventApplicationId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        eventApplicationService.deleteEventApplication(eventApplicationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/event/{eventId}")
    @RequiresRole({UserType.VOLUNTEER, UserType.ADMIN})
    public ResponseEntity<List<EventApplicationDTO>> getEventApplicationsByEventId(@PathVariable Long eventId) {
        if (eventId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var selectedApplications = eventApplicationService.getEventApplicationsByEventId(eventId);
        return new ResponseEntity<>(selectedApplications, HttpStatus.OK);
    }

    @GetMapping("/event/volunteers/{eventId}")
    @RequiresRole({UserType.VOLUNTEER, UserType.ADMIN})
    public ResponseEntity<List<EventApplicationAndVolDTO>> getEventApplicationsAndVolunteersByEventId(
            @PathVariable Long eventId) {

        List<EventApplicationAndVolDTO> selectedApplications = eventApplicationService
                .getEventApplicationsAndVolunteersByEventId(eventId);

        return ResponseEntity.ok(selectedApplications);
    }
}
