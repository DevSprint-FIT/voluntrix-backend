package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.DevSprint.voluntrix_backend.services.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/organizations")
@SecurityRequirement(name = "bearerAuth")
public class OrganizationController {
    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDTO> getOrganizationDetails(@PathVariable Long id) {
        Optional<OrganizationDTO> organizationDTO = organizationService.getOrganizationDetails(id);
        return organizationDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations() {
        List<OrganizationDTO> organizationDTOs = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizationDTOs);
    }

    @PostMapping
    public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
        OrganizationDTO createdOrganization = organizationService.createOrganization(organizationDTO);
        return ResponseEntity.status(201).body(createdOrganization);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDTO> updateOrganization(@PathVariable Long id, @RequestBody OrganizationDTO updatedDTO) {
        Optional<OrganizationDTO> updatedOrganization = organizationService.updateOrganization(id, updatedDTO);
        return updatedOrganization.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        boolean deleted = organizationService.deleteOrganization(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
