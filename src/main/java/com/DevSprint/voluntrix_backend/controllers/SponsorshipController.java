package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/sponsorships")
@Validated
public class SponsorshipController {

    private final SponsorshipService sponsorshipService;

    @PostMapping
    public ResponseEntity<ApiResponse<SponsorshipDTO>> createSponsorship(@Valid @RequestBody SponsorshipCreateDTO sponsorshipCreateDTO) {
        SponsorshipDTO createdSponsorship = sponsorshipService.createSponsorship(sponsorshipCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Sponsorship created successfully", createdSponsorship));
    }

    @GetMapping("/{sponsorshipId}")
    public ResponseEntity<ApiResponse<SponsorshipDTO>> getSponsorshipById(@PathVariable Long sponsorshipId) {
        SponsorshipDTO sponsorship = sponsorshipService.getSponsorshipById(sponsorshipId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship retrieved successfully", sponsorship));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponse<List<SponsorshipDTO>>> getSponsorshipsByEventId(@PathVariable Long eventId) {
        List<SponsorshipDTO> sponsorships = sponsorshipService.getSponsorshipsByEventId(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorships retrieved successfully", sponsorships));
    }

    @GetMapping("/event/{eventId}/available")
    public ResponseEntity<ApiResponse<List<SponsorshipDTO>>> getAvailableSponsorshipsByEventId(@PathVariable Long eventId) {
        List<SponsorshipDTO> sponsorships = sponsorshipService.getAvailableSponsorshipsByEventId(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Available sponsorships retrieved successfully", sponsorships));
    }

    @DeleteMapping("/{sponsorshipId}")
    public ResponseEntity<ApiResponse<String>> deleteSponsorship(@PathVariable Long sponsorshipId) {
        sponsorshipService.deleteSponsorship(sponsorshipId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship deleted successfully", null));
    }

    @PostMapping("/{sponsorshipId}/availability")
    public ResponseEntity<ApiResponse<SponsorshipDTO>> updateSponsorshipAvailability(@PathVariable Long sponsorshipId, @RequestBody boolean isAvailable) {
        SponsorshipDTO updatedSponsorship = sponsorshipService.updateSponsorshipAvailability(sponsorshipId, isAvailable);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship availability updated successfully", updatedSponsorship));
    }
}
