package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipDTO;
import com.DevSprint.voluntrix_backend.services.SponsorshipService;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.UserType;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sponsorships")
@Validated
@SecurityRequirement(name = "bearerAuth")
public class SponsorshipController {

    private final SponsorshipService sponsorshipService;

    @PostMapping
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<ApiResponse<SponsorshipDTO>> createSponsorship(@Valid @RequestBody SponsorshipCreateDTO sponsorshipCreateDTO) {
        SponsorshipDTO createdSponsorship = sponsorshipService.createSponsorship(sponsorshipCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Sponsorship created successfully", createdSponsorship));
    }

    @GetMapping("/{sponsorshipId}")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION, UserType.SPONSOR})
    public ResponseEntity<ApiResponse<SponsorshipDTO>> getSponsorshipById(@PathVariable Long sponsorshipId) {
        SponsorshipDTO sponsorship = sponsorshipService.getSponsorshipById(sponsorshipId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship retrieved successfully", sponsorship));
    }

    @GetMapping("/event/{eventId}")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION, UserType.SPONSOR})
    public ResponseEntity<ApiResponse<List<SponsorshipDTO>>> getSponsorshipsByEventId(@PathVariable Long eventId) {
        List<SponsorshipDTO> sponsorships = sponsorshipService.getSponsorshipsByEventId(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorships retrieved successfully", sponsorships));
    }

    @GetMapping("/event/{eventId}/available")
    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION, UserType.SPONSOR})
    public ResponseEntity<ApiResponse<List<SponsorshipDTO>>> getAvailableSponsorshipsByEventId(@PathVariable Long eventId) {
        List<SponsorshipDTO> sponsorships = sponsorshipService.getAvailableSponsorshipsByEventId(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Available sponsorships retrieved successfully", sponsorships));
    }

    @DeleteMapping("/{sponsorshipId}")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<ApiResponse<String>> deleteSponsorship(@PathVariable Long sponsorshipId) {
        sponsorshipService.deleteSponsorship(sponsorshipId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship deleted successfully", null));
    }

    @PostMapping("/{sponsorshipId}/availability")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<ApiResponse<SponsorshipDTO>> updateSponsorshipAvailability(@PathVariable Long sponsorshipId, @RequestBody boolean isAvailable) {
        SponsorshipDTO updatedSponsorship = sponsorshipService.updateSponsorshipAvailability(sponsorshipId, isAvailable);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship availability updated successfully", updatedSponsorship));
    }
}
