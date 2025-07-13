package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerEventStatsDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerActiveEventDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerAppliedEventDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCompletedEventDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerEventParticipationCreateDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.services.EventService;
import com.DevSprint.voluntrix_backend.services.VolunteerService;
import com.DevSprint.voluntrix_backend.services.VolunteerEventParticipationService;
import com.DevSprint.voluntrix_backend.dtos.EventLeaderboardDTO;

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
@RequestMapping("/api/public/participations")
@SecurityRequirement(name = "bearerAuth")
public class VolunteerEventParticipationController {

    private final VolunteerEventParticipationService participationService;
    private final VolunteerService volunteerService;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<VolunteerEventParticipationDTO> createParticipation(@RequestBody VolunteerEventParticipationCreateDTO createDTO) {
        VolunteerEntity volunteer = volunteerService.getVolunteerById(createDTO.getVolunteerId());
        EventEntity event = eventService.getEventEntityById(createDTO.getEventId());

        VolunteerEventParticipationDTO created = participationService.createParticipation(createDTO, volunteer, event);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<VolunteerEventParticipationDTO>> getByVolunteer(@PathVariable Long volunteerId) {
        List<VolunteerEventParticipationDTO> participations = participationService.getParticipationsByVolunteer(volunteerId);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<VolunteerEventParticipationDTO>> getByEvent(@PathVariable Long eventId) {
        List<VolunteerEventParticipationDTO> participations = participationService.getParticipationsByEvent(eventId);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/volunteer/{volunteerId}/event/{eventId}")
    public ResponseEntity<VolunteerEventParticipationDTO> getByVolunteerAndEvent(@PathVariable Long volunteerId, @PathVariable Long eventId) {
        VolunteerEventParticipationDTO dto = participationService.getParticipationByVolunteerAndEvent(volunteerId, eventId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/volunteer/{volunteerId}/stats")
    public ResponseEntity<VolunteerEventStatsDTO> getVolunteerEventStats(@PathVariable Long volunteerId) {
        return ResponseEntity.ok(participationService.getVolunteerEventStats(volunteerId));
    }

    @GetMapping("/volunteers/{volunteerId}/active-events")
    public ResponseEntity<List<VolunteerActiveEventDTO>> getActiveEventsForVolunteer(@PathVariable Long volunteerId) {
        List<VolunteerActiveEventDTO> activeEvents = participationService.getActiveEventsByVolunteerId(volunteerId);
        return ResponseEntity.ok(activeEvents);
    }

    @GetMapping("/volunteers/{volunteerId}/completed-events")
    public ResponseEntity<List<VolunteerCompletedEventDTO>> getCompletedEvents(@PathVariable Long volunteerId) {
        List<VolunteerCompletedEventDTO> completedEvents = participationService.getCompletedEventsByVolunteerId(volunteerId);
        return ResponseEntity.ok(completedEvents);
    }

    @GetMapping("/volunteers/{volunteerId}/applied-events")
    public ResponseEntity<List<VolunteerAppliedEventDTO>> getAppliedEvents(@PathVariable Long volunteerId) {
        List<VolunteerAppliedEventDTO> appliedEvents = participationService.getAppliedEventsByVolunteerId(volunteerId);
        return ResponseEntity.ok(appliedEvents);
    }

    @DeleteMapping("/volunteer/{volunteerId}/event/{eventId}")
    public ResponseEntity<Void> deleteParticipation(@PathVariable Long volunteerId, @PathVariable Long eventId) {
        participationService.deleteParticipationByVolunteerAndEvent(volunteerId, eventId);
        return ResponseEntity.noContent().build();
    }

    // Get event leaderboard
    @GetMapping("/event/{eventId}/leaderboard")
    public ResponseEntity<List<EventLeaderboardDTO>> getEventLeaderboard(@PathVariable Long eventId) {
        List<EventLeaderboardDTO> leaderboard = participationService.getEventLeaderboard(eventId);
        return ResponseEntity.ok(leaderboard);
    }
}
