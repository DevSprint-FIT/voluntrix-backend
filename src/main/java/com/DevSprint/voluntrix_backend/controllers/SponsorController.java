package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.SponsorCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorUpdateDTO;
import com.DevSprint.voluntrix_backend.services.SponsorService;
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
@RequestMapping("/api/sponsors")
@SecurityRequirement(name = "bearerAuth")
public class SponsorController {
    
    private final SponsorService sponsorService;
    private final CurrentUserService currentUserService;

    @GetMapping("/all")
    @RequiresRole({UserType.VOLUNTEER, UserType.ADMIN, UserType.ORGANIZATION, UserType.SPONSOR})
    public ResponseEntity<ApiResponse<List<SponsorDTO>>> getAllSponsors() {
        List<SponsorDTO> sponsors = sponsorService.getAllSponsors();
        return ResponseEntity.ok(new ApiResponse<>("Sponsors retrieved successfully", sponsors));
    }

    @GetMapping("/me")
    @RequiresRole(UserType.SPONSOR)
    public ResponseEntity<ApiResponse<SponsorDTO>> getSponsorByUsername() {
        Long userId = currentUserService.getCurrentUserId();
        SponsorDTO sponsor = sponsorService.getSponsorByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsor retrieved successfully", sponsor));
    }

    @PostMapping("/")
    @RequiresRole(UserType.SPONSOR)
    public ResponseEntity<ApiResponse<SponsorDTO>> createSponsor(@Valid @RequestBody SponsorCreateDTO sponsorCreateDTO) {
        Long userId = currentUserService.getCurrentUserId();
        SponsorDTO createdSponsor = sponsorService.createSponsor(sponsorCreateDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Sponsor created successfully", createdSponsor));
    }

    @PatchMapping("/profile")
    @RequiresRole(UserType.SPONSOR)
    public ResponseEntity<ApiResponse<SponsorDTO>> updateSponsorProfile(@Valid @RequestBody SponsorUpdateDTO sponsorUpdateDTO) {
        Long userId = currentUserService.getCurrentUserId();
        SponsorDTO updatedSponsor = sponsorService.updateSponsorProfile(sponsorUpdateDTO, userId);
        return ResponseEntity.ok(new ApiResponse<>("Sponsor updated successfully", updatedSponsor));
    }
}
