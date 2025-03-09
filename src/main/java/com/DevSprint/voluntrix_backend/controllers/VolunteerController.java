package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.services.VolunteerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/volunteers")
@SecurityRequirement(name = "bearerAuth")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping
    public ResponseEntity<List<VolunteerDTO>> getAllVolunteers() {
        List<VolunteerDTO> volunteers = volunteerService.getAllVolunteers();
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/{volunteerId}")
    public ResponseEntity<VolunteerDTO> getVolunteerById(@PathVariable Long volunteerId) {
        VolunteerDTO volunteer = volunteerService.getVolunteerById(volunteerId);
        if (volunteer != null) {
            return ResponseEntity.ok(volunteer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<VolunteerDTO> createVolunteer(@RequestBody VolunteerDTO volunteerDTO) {
        VolunteerDTO createdVolunteer = volunteerService.createVolunteer(volunteerDTO);
        return ResponseEntity.status(201).body(createdVolunteer);
    }

    @PutMapping("/{volunteerId}")
    public ResponseEntity<VolunteerDTO> updateVolunteer(@PathVariable Long volunteerId, @RequestBody VolunteerDTO volunteerDTO) {
        VolunteerDTO updatedVolunteer = volunteerService.updateVolunteer(volunteerId, volunteerDTO);
        if (updatedVolunteer != null) {
            return ResponseEntity.ok(updatedVolunteer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{volunteerId}")
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Long volunteerId) {
        boolean isDeleted = volunteerService.deleteVolunteer(volunteerId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();  // 204 status code for successful delete
        } else {
            return ResponseEntity.notFound().build();  // 404 status code if volunteer not found
        }
    }
}

