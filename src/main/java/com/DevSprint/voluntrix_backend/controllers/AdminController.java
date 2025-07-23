package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import com.DevSprint.voluntrix_backend.services.SponsorService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Administrative operations")
public class AdminController {
    
    private final SponsorService sponsorService;

    @GetMapping("/sponsors/unverified")
    @RequiresRole(UserType.ADMIN)
    public ResponseEntity<ApiResponse<List<SponsorDTO>>> getUnverifiedSponsors() {
        List<SponsorDTO> unverifiedSponsors = sponsorService.getUnverifiedSponsors();
        return ResponseEntity.ok(new ApiResponse<>("Unverified sponsors retrieved successfully", unverifiedSponsors));
    }

    @PatchMapping("/sponsors/{sponsorId}/verify")
    @RequiresRole(UserType.ADMIN)
    public ResponseEntity<ApiResponse<SponsorDTO>> verifySponsor(
            @Parameter(description = "ID of the sponsor to verify/unverify") @PathVariable Long sponsorId,
            @Parameter(description = "Verification status (true to verify, false to unverify)") @RequestParam boolean verified) {
        SponsorDTO updatedSponsor = sponsorService.verifySponsor(sponsorId, verified);
        String message = verified ? "Sponsor verified successfully" : "Sponsor unverified successfully";
        return ResponseEntity.ok(new ApiResponse<>(message, updatedSponsor));
    }

    @GetMapping("/sponsors/all")
    @RequiresRole(UserType.ADMIN)
    public ResponseEntity<ApiResponse<List<SponsorDTO>>> getAllSponsorsForAdmin() {
        List<SponsorDTO> sponsors = sponsorService.getAllSponsors();
        return ResponseEntity.ok(new ApiResponse<>("All sponsors retrieved successfully", sponsors));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordDTO {
        @NotBlank(message = "Current password is required")
        private String currentPassword;
        
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        private String newPassword;
        
        @NotBlank(message = "Password confirmation is required")
        private String confirmPassword;
    }
}
