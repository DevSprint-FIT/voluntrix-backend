package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerUpdateDTO;
import com.DevSprint.voluntrix_backend.services.VolunteerService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/volunteers")
@SecurityRequirement(name = "bearerAuth")
public class VolunteerController {

    private final VolunteerService volunteerService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public ResponseEntity<List<VolunteerDTO>> getAllVolunteers() {
        List<VolunteerDTO> volunteers = volunteerService.getAllVolunteers();
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/{username}")
    public ResponseEntity<VolunteerDTO> getVolunteerByUsername(@PathVariable String username) {
        // Fetches a volunteer by username. Throws VolunteerNotFoundException if not found.
        VolunteerDTO volunteer = volunteerService.getVolunteerByUsername(username);
        return ResponseEntity.ok(volunteer);
    }

    @PostMapping
    public ResponseEntity<VolunteerDTO> createVolunteer(@Valid @RequestBody VolunteerCreateDTO volunteerCreateDTO) {
        Long userId = currentUserService.getCurrentUserId();
        VolunteerDTO createdVolunteer = volunteerService.createVolunteer(userId, volunteerCreateDTO);
        return ResponseEntity.status(201).body(createdVolunteer);
    }

    @PatchMapping("/{volunteerId}")
    public ResponseEntity<VolunteerDTO> updateVolunteer(@PathVariable Long volunteerId, @Valid @RequestBody VolunteerUpdateDTO volunteerUpdateDTO) {
        // Updates volunteer details by ID. Throws VolunteerNotFoundException if not found.
        VolunteerDTO updatedVolunteer = volunteerService.patchVolunteer(volunteerId, volunteerUpdateDTO);
        return ResponseEntity.ok(updatedVolunteer); 
    }

    @DeleteMapping("/{volunteerId}")
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Long volunteerId) {
        // Deletes a volunteer by ID. Throws VolunteerNotFoundException if not found.
        volunteerService.deleteVolunteer(volunteerId);
        return ResponseEntity.noContent().build(); 
    }
}
