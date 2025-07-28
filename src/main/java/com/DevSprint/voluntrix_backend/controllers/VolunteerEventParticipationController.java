package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationDTO;
import com.DevSprint.voluntrix_backend.dtos.EventLeaderboardDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerActiveEventDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCompletedEventDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerAppliedEventDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerEventStatsDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.services.EventService;
import com.DevSprint.voluntrix_backend.services.VolunteerService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.services.VolunteerEventParticipationService;
import com.DevSprint.voluntrix_backend.enums.UserType;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/participations")
@SecurityRequirement(name = "bearerAuth")
public class VolunteerEventParticipationController {

    private final VolunteerEventParticipationService participationService;
    private final VolunteerService volunteerService;
    private final EventService eventService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<VolunteerEventParticipationDTO> createParticipation(@RequestBody VolunteerEventParticipationCreateDTO createDTO) {
        VolunteerEntity volunteer = volunteerService.getVolunteerById(createDTO.getVolunteerId());
        EventEntity event = eventService.getEventEntityById(createDTO.getEventId());

        VolunteerEventParticipationDTO created = participationService.createParticipation(createDTO, volunteer, event);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/volunteer/all")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<List<VolunteerEventParticipationDTO>> getByVolunteer() {
        Long volunteerId = currentUserService.getCurrentEntityId();
        List<VolunteerEventParticipationDTO> participations = participationService.getParticipationsByVolunteer(volunteerId);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/event/{eventId}")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION})
    public ResponseEntity<List<VolunteerEventParticipationDTO>> getByEvent(@PathVariable Long eventId) {
        List<VolunteerEventParticipationDTO> participations = participationService.getParticipationsByEvent(eventId);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/event/{eventId}/available")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION})
    public ResponseEntity<List<VolunteerEventParticipationDTO>> getAvailableVolunteersByEvent(@PathVariable Long eventId) {
        List<VolunteerEventParticipationDTO> participations = participationService.getAvailableParticipationsByEvent(eventId);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/volunteer/event/{eventId}")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<VolunteerEventParticipationDTO> getByVolunteerAndEvent(@PathVariable Long eventId) {
        Long volunteerId = currentUserService.getCurrentEntityId();
        VolunteerEventParticipationDTO dto = participationService.getParticipationByVolunteerAndEvent(volunteerId, eventId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/volunteer/stats")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<VolunteerEventStatsDTO> getVolunteerEventStats() {
        Long volunteerId = currentUserService.getCurrentEntityId();
        return ResponseEntity.ok(participationService.getVolunteerEventStats(volunteerId));
    }

    @GetMapping("/volunteers/active-events")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<List<VolunteerActiveEventDTO>> getActiveEventsForVolunteer() {
        Long volunteerId = currentUserService.getCurrentEntityId();
        List<VolunteerActiveEventDTO> activeEvents = participationService.getActiveEventsByVolunteerId(volunteerId);
        return ResponseEntity.ok(activeEvents);
    }

    @GetMapping("/volunteers/completed-events")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<List<VolunteerCompletedEventDTO>> getCompletedEvents() {
        Long volunteerId = currentUserService.getCurrentEntityId();
        List<VolunteerCompletedEventDTO> completedEvents = participationService.getCompletedEventsByVolunteerId(volunteerId);
        return ResponseEntity.ok(completedEvents);
    }

    @GetMapping("/volunteers/applied-events")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<List<VolunteerAppliedEventDTO>> getAppliedEvents() {
        Long volunteerId = currentUserService.getCurrentEntityId();
        List<VolunteerAppliedEventDTO> appliedEvents = participationService.getAppliedEventsByVolunteerId(volunteerId);
        return ResponseEntity.ok(appliedEvents);
    }

    @DeleteMapping("/volunteer/event/{eventId}")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<Void> deleteParticipation(@PathVariable Long eventId) {
        Long volunteerId = currentUserService.getCurrentEntityId();
        participationService.deleteParticipationByVolunteerAndEvent(volunteerId, eventId);
        return ResponseEntity.noContent().build();
    }

    // Get event leaderboard
    @GetMapping("/event/{eventId}/leaderboard")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION})
    public ResponseEntity<List<EventLeaderboardDTO>> getEventLeaderboard(@PathVariable Long eventId) {
        List<EventLeaderboardDTO> leaderboard = participationService.getEventLeaderboard(eventId);
        return ResponseEntity.ok(leaderboard);
    }
}
