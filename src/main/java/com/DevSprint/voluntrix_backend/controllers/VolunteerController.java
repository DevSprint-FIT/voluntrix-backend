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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import jakarta.validation.Valid; 
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.UserType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/volunteers")
@SecurityRequirement(name = "bearerAuth")
public class VolunteerController {

    private final VolunteerService volunteerService;
    private final CurrentUserService currentUserService;

    @GetMapping("/me")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<VolunteerDTO> getVolunteerByUsername() {
        Long userId = currentUserService.getCurrentUserId();
        VolunteerDTO volunteer = volunteerService.getVolunteerByUserId(userId);
        return ResponseEntity.ok(volunteer);
    }

    @PostMapping
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<VolunteerDTO> createVolunteer(@Valid @RequestBody VolunteerCreateDTO volunteerCreateDTO) {
        Long userId = currentUserService.getCurrentUserId();
        VolunteerDTO createdVolunteer = volunteerService.createVolunteer(volunteerCreateDTO, userId);
        return ResponseEntity.status(201).body(createdVolunteer);
    }

    @PatchMapping("/profile")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<VolunteerDTO> updateVolunteerProfile(@Valid @RequestBody VolunteerUpdateDTO volunteerUpdateDTO) {
        Long userId = currentUserService.getCurrentUserId();
        VolunteerDTO updatedVolunteer = volunteerService.patchVolunteerProfile(userId, volunteerUpdateDTO);
        return ResponseEntity.ok(updatedVolunteer); 
    }

    @PatchMapping("/promote-to-event-host")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<VolunteerDTO> promoteToEventHost() {
        Long userId = currentUserService.getCurrentUserId();
        VolunteerDTO updatedVolunteer = volunteerService.promoteToEventHost(userId);
        return ResponseEntity.ok(updatedVolunteer);
    }
}
