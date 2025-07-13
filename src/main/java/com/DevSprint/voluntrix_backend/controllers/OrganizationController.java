package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.OrganizationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.exceptions.BadRequestException;
import com.DevSprint.voluntrix_backend.services.OrganizationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/organizations")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationDTO>> getOrganizationDetails(@PathVariable Long id) {
        OrganizationDTO organization = organizationService.getOrganizationDetails(id);
        return ResponseEntity.ok(new ApiResponse<>("Organization retrieved successfully", organization));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<ApiResponse<OrganizationDTO>> getByUsername(@PathVariable String username) {
        OrganizationDTO organization = organizationService.getOrganizationByUsername(username);
        return ResponseEntity.ok(new ApiResponse<>("Organization retrieved successfully", organization));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganizationDTO>>> getAllOrganizations() {
        List<OrganizationDTO> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(new ApiResponse<>("Organizations retrieved successfully", organizations));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationDTO>> createOrganization(@Valid @RequestBody OrganizationCreateDTO organizationCreateDTO) {
        OrganizationDTO created = organizationService.createOrganization(organizationCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Organization created successfully", created));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationDTO>> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationDTO organizationDTO) {
        if (id == null) {
            throw new BadRequestException("Invalid ID");
        }
        
        OrganizationDTO updatedOrganization = organizationService.updateOrganization(id, organizationDTO);
        return ResponseEntity.ok(new ApiResponse<>("Organization updated successfully", updatedOrganization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok(new ApiResponse<>("Organization deleted successfully", null));
    }
}