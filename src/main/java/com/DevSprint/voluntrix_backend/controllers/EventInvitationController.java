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

import com.DevSprint.voluntrix_backend.dtos.EventInvitationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventInvitationDTO;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.EventInvitationService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/event-invitations")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EventInvitationController {

    private final EventInvitationService eventInvitationService;

    @GetMapping
    @RequiresRole({UserType.ADMIN, UserType.VOLUNTEER})
    public ResponseEntity<List<EventInvitationDTO>> getAllEventInvitations() {
        return new ResponseEntity<List<EventInvitationDTO>>(eventInvitationService.getAllEventInvitations(),
                HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<Void> addEventInvitation(@RequestBody EventInvitationCreateDTO eventInvitationCreateDTO) {
        if (eventInvitationCreateDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        eventInvitationService.addEventInvitation(eventInvitationCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{eventInvitationId}")
    @RequiresRole({UserType.ADMIN, UserType.VOLUNTEER})
    public ResponseEntity<EventInvitationDTO> getEventInvitationById(@PathVariable Long eventInvitationId) {
        if (eventInvitationId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var selectedInvitation = eventInvitationService.getEventInvitationById(eventInvitationId);
        return new ResponseEntity<>(selectedInvitation, HttpStatus.OK);
    }

    @PatchMapping(value = "/{eventInvitationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<Void> updateEventInvitation(@PathVariable Long eventInvitationId,
            @Valid @RequestBody EventInvitationCreateDTO eventInvitationCreateDTO) {
        if (eventInvitationId == null || eventInvitationCreateDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        eventInvitationService.updateEventInvitation(eventInvitationCreateDTO, eventInvitationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{eventInvitationId}")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<Void> deleteEventInvitation(@PathVariable Long eventInvitationId) {
        if (eventInvitationId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        eventInvitationService.deleteEventInvitation(eventInvitationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/organization/{organizationId}")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION})
    public ResponseEntity<List<EventInvitationDTO>> getEventInvitationsByOrganizationId(@PathVariable Long organizationId) {
        if (organizationId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var selectedInvitations = eventInvitationService.getEventInvitationsByOrganizationId(organizationId);
        return new ResponseEntity<>(selectedInvitations, HttpStatus.OK);
    }
}
