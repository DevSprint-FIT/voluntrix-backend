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

import com.DevSprint.voluntrix_backend.dtos.SponsorRequestTableDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipRequestCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipRequestDTO;
import com.DevSprint.voluntrix_backend.services.SponsorshipRequestService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.SponsorshipRequestStatus;
import com.DevSprint.voluntrix_backend.enums.UserType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sponsorship-requests")
@Validated
public class SponsorshipRequestController {
    
    private final SponsorshipRequestService sponsorshipRequestService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @RequiresRole(UserType.SPONSOR)
    public ResponseEntity<ApiResponse<SponsorshipRequestDTO>> createSponsorshipRequest(@Valid @RequestBody SponsorshipRequestCreateDTO createDTO) {
        SponsorshipRequestDTO createdRequest = sponsorshipRequestService.createSponsorshipRequest(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Sponsorship request created successfully", createdRequest));
    }


    @GetMapping("/sponsor/my-requests")
    @RequiresRole(UserType.SPONSOR)
    public ResponseEntity<ApiResponse<List<SponsorshipRequestDTO>>> getSponsorshipRequestsBySponsorId() {
        Long sponsorId = currentUserService.getCurrentEntityId();
        List<SponsorshipRequestDTO> requests = sponsorshipRequestService.getSponsorshipRequestsBySponsorId(sponsorId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship requests retrieved successfully", requests));
    }

    @GetMapping("/event/{eventId}")
    @RequiresRole({UserType.ORGANIZATION, UserType.VOLUNTEER})
    public ResponseEntity<ApiResponse<List<SponsorshipRequestDTO>>> getSponsorshipRequestsByEventId(@PathVariable Long eventId) {
        List<SponsorshipRequestDTO> requests = sponsorshipRequestService.getSponsorshipRequestsByEventId(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship requests retrieved successfully", requests));
    }

    @GetMapping("/sponsor/status/{status}")
    @RequiresRole(UserType.SPONSOR)
    public ResponseEntity<ApiResponse<List<SponsorRequestTableDTO>>> getSponsorshipRequestsBySponsorIdAndStatus(
            @PathVariable SponsorshipRequestStatus status) {
        Long sponsorId = currentUserService.getCurrentEntityId();
        List<SponsorRequestTableDTO> requests = sponsorshipRequestService.getSponsorshipRequestsBySponsorIdAndStatus(sponsorId, status);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship requests retrieved successfully", requests));
    }

    @GetMapping("/event/{eventId}/status/{status}")
    @RequiresRole({UserType.ORGANIZATION, UserType.VOLUNTEER})
    public ResponseEntity<ApiResponse<List<SponsorshipRequestDTO>>> getSponsorshipRequestsByEventIdAndStatus(
            @PathVariable Long eventId, @PathVariable String status) {
        List<SponsorshipRequestDTO> requests = sponsorshipRequestService.getSponsorshipRequestsByEventIdAndStatus(eventId, status);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship requests retrieved successfully", requests));
    }

    @PatchMapping("/{requestId}/status/{status}")
    @RequiresRole(UserType.SPONSOR)
    public ResponseEntity<ApiResponse<SponsorshipRequestDTO>> updateSponsorshipRequestStatus(
            @PathVariable Long requestId, @PathVariable String status) {
        SponsorshipRequestDTO updatedRequest = sponsorshipRequestService.updateSponsorshipRequestStatus(requestId, status);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship request status updated successfully", updatedRequest));
    }

    @DeleteMapping("/{requestId}")
    @RequiresRole(UserType.SPONSOR)
    public ResponseEntity<ApiResponse<String>> deleteSponsorshipRequest(@PathVariable Long requestId) {
        sponsorshipRequestService.deleteSponsorshipRequest(requestId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsorship request deleted successfully", null));
    }

}
