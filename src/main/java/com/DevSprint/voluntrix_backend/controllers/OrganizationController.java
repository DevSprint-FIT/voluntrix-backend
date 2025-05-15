package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.OrganizationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.exceptions.BadRequestException;
import com.DevSprint.voluntrix_backend.exceptions.ResourceNotFoundException;
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
        try {
            OrganizationDTO organization = organizationService.getOrganizationDetails(id);
            return ResponseEntity.ok(new ApiResponse<>("Organization retrieved successfully", organization));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse<>("An unexpected error occurred", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<ApiResponse<OrganizationDTO>> getByUsername(@PathVariable String username) {
        try {
            OrganizationDTO organization = organizationService.getOrganizationByUsername(username);
            return ResponseEntity.ok(new ApiResponse<>("Organization retrieved successfully", organization));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse<>("An unexpected error occurred", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganizationDTO>>> getAllOrganizations() {
        try {
            List<OrganizationDTO> organizations = organizationService.getAllOrganizations();
            return ResponseEntity.ok(new ApiResponse<>("Organizations retrieved successfully", organizations));
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse<>("An unexpected error occurred", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationDTO>> createOrganization(@Valid @RequestBody OrganizationCreateDTO organizationCreateDTO) {
        try {
            OrganizationDTO created = organizationService.createOrganization(organizationCreateDTO);
            return ResponseEntity.status(201).body(new ApiResponse<>("Organization created successfully", created));
        } catch (BadRequestException ex) {
            return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse<>("An unexpected error occurred", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationDTO>> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationDTO organizationDTO) {  // Accepting the request body
        if (id == null) {
            return new ResponseEntity<>(new ApiResponse<>("Invalid ID", null), HttpStatus.BAD_REQUEST);
        }

        try {
            OrganizationDTO updatedOrganization = organizationService.updateOrganization(id, organizationDTO);
            return ResponseEntity.ok(new ApiResponse<>("Organization updated successfully", updatedOrganization));
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse<>("An unexpected error occurred", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrganization(@PathVariable Long id) {
        try {
            organizationService.deleteOrganization(id);
            return ResponseEntity.ok(new ApiResponse<>("Organization deleted successfully", null));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse<>("An unexpected error occurred", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
