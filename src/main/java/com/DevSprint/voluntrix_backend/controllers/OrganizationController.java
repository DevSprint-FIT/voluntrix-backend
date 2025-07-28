package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.OrganizationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationUpdateDTO;
import com.DevSprint.voluntrix_backend.services.OrganizationService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<List<OrganizationDTO>>> getAllOrganizations() {
        List<OrganizationDTO> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(new ApiResponse<>("Organizations retrieved successfully", organizations));
    }

    @GetMapping("/me")
    @RequiresRole(UserType.ORGANIZATION)
    public ResponseEntity<ApiResponse<OrganizationDTO>> getOrganizationByUsername() {
        Long userId = currentUserService.getCurrentUserId();
        OrganizationDTO organization = organizationService.getOrganizationByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>("Organization retrieved successfully", organization));
    }

    @PostMapping("/")
    @RequiresRole(UserType.ORGANIZATION)
    public ResponseEntity<ApiResponse<OrganizationDTO>> createOrganization(@Valid @RequestBody OrganizationCreateDTO organizationCreateDTO) {
        Long userId = currentUserService.getCurrentUserId();
        OrganizationDTO createdOrganization = organizationService.createOrganization(organizationCreateDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Organization created successfully", createdOrganization));
    }

    @PatchMapping("/profile")
    @RequiresRole(UserType.ORGANIZATION)
    public ResponseEntity<ApiResponse<OrganizationDTO>> updateOrganizationProfile(@Valid @RequestBody OrganizationUpdateDTO organizationUpdateDTO) {
        Long userId = currentUserService.getCurrentUserId();
        OrganizationDTO updatedOrganization = organizationService.updateOrganizationProfile(organizationUpdateDTO, userId);
        return ResponseEntity.ok(new ApiResponse<>("Organization updated successfully", updatedOrganization));
    }
}