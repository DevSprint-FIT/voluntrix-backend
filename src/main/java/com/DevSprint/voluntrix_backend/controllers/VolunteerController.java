package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerUpdateDTO;
import com.DevSprint.voluntrix_backend.services.VolunteerService;
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

    @GetMapping("/{username}")
    public ResponseEntity<VolunteerDTO> getVolunteerByUsername(@PathVariable String username) {
        VolunteerDTO volunteer = volunteerService.getVolunteerByUsername(username);
        if (volunteer != null) {
            return ResponseEntity.ok(volunteer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<VolunteerDTO> createVolunteer(@RequestBody VolunteerCreateDTO volunteerCreateDTO) {
        VolunteerDTO createdVolunteer = volunteerService.createVolunteer(volunteerCreateDTO);
        return ResponseEntity.status(201).body(createdVolunteer);
    }

    @PatchMapping("/{volunteerId}")
    public ResponseEntity<VolunteerDTO> updateVolunteer(@PathVariable Long volunteerId, @RequestBody VolunteerUpdateDTO volunteerUpdateDTO) {
        VolunteerDTO updatedVolunteer = volunteerService.patchVolunteer(volunteerId, volunteerUpdateDTO);
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
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
