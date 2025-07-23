package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.OrganizationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationUpdateDTO;
import com.DevSprint.voluntrix_backend.services.OrganizationService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.UserType;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/organizations")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationController {
    
    private final OrganizationService organizationService;
    private final CurrentUserService currentUserService;

    @GetMapping("/all")
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations() {
        List<OrganizationDTO> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/me")
    @RequiresRole(UserType.ORGANIZATION)
    public ResponseEntity<OrganizationDTO> getOrganizationByUsername() {
        Long userId = currentUserService.getCurrentUserId();
        OrganizationDTO organization = organizationService.getOrganizationByUserId(userId);
        return ResponseEntity.ok(organization);
    }

    @PostMapping
    @RequiresRole(UserType.ORGANIZATION)
    public ResponseEntity<OrganizationDTO> createOrganization(@Valid @RequestBody OrganizationCreateDTO organizationCreateDTO) {
        Long userId = currentUserService.getCurrentUserId();
        OrganizationDTO createdOrganization = organizationService.createOrganization(organizationCreateDTO, userId);
        return ResponseEntity.status(201).body(createdOrganization);
    }

    @PatchMapping("/profile")
    @RequiresRole(UserType.ORGANIZATION)
    public ResponseEntity<OrganizationDTO> updateOrganizationProfile(@Valid @RequestBody OrganizationUpdateDTO organizationUpdateDTO) {
        Long userId = currentUserService.getCurrentUserId();
        OrganizationDTO updatedOrganization = organizationService.updateOrganizationProfile(organizationUpdateDTO, userId);
        return ResponseEntity.ok(updatedOrganization);
    }
}