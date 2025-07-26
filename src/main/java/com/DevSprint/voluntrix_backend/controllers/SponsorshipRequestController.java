package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipRequestCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipRequestDTO;
import com.DevSprint.voluntrix_backend.services.SponsorshipRequestService;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/sponsorship-requests")
@Validated
public class SponsorshipRequestController {
    
    private final SponsorshipRequestService sponsorshipRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<SponsorshipRequestDTO>> createSponsorshipRequest(@Valid @RequestBody SponsorshipRequestCreateDTO createDTO) {
        SponsorshipRequestDTO createdRequest = sponsorshipRequestService.createSponsorshipRequest(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Sponsorship request created successfully", createdRequest));
    }


    @GetMapping("/sponsor/{sponsorId}")
    public ResponseEntity<ApiResponse<List<SponsorshipRequestDTO>>> getSponsorshipRequestsBySponsorId(@PathVariable Long sponsorId) {
        List<SponsorshipRequestDTO> requests = sponsorshipRequestService.getSponsorshipRequestsBySponsorId(sponsorId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship requests retrieved successfully", requests));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponse<List<SponsorshipRequestDTO>>> getSponsorshipRequestsByEventId(@PathVariable Long eventId) {
        List<SponsorshipRequestDTO> requests = sponsorshipRequestService.getSponsorshipRequestsByEventId(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship requests retrieved successfully", requests));
    }

    @GetMapping("/sponsor/{sponsorId}/status/{status}")
    public ResponseEntity<ApiResponse<List<SponsorshipRequestDTO>>> getSponsorshipRequestsBySponsorIdAndStatus(
            @PathVariable Long sponsorId, @PathVariable String status) {
        List<SponsorshipRequestDTO> requests = sponsorshipRequestService.getSponsorshipRequestsBySponsorIdAndStatus(sponsorId, status);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship requests retrieved successfully", requests));
    }

    @GetMapping("/event/{eventId}/status/{status}")
    public ResponseEntity<ApiResponse<List<SponsorshipRequestDTO>>> getSponsorshipRequestsByEventIdAndStatus(
            @PathVariable Long eventId, @PathVariable String status) {
        List<SponsorshipRequestDTO> requests = sponsorshipRequestService.getSponsorshipRequestsByEventIdAndStatus(eventId, status);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship requests retrieved successfully", requests));
    }

    @PatchMapping("/{requestId}/status/{status}")
    public ResponseEntity<ApiResponse<SponsorshipRequestDTO>> updateSponsorshipRequestStatus(
            @PathVariable Long requestId, @PathVariable String status) {
        SponsorshipRequestDTO updatedRequest = sponsorshipRequestService.updateSponsorshipRequestStatus(requestId, status);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship request status updated successfully", updatedRequest));
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<ApiResponse<String>> deleteSponsorshipRequest(@PathVariable Long requestId) {
        sponsorshipRequestService.deleteSponsorshipRequest(requestId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship request deleted successfully", null));
    }

}
