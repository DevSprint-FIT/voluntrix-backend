package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.InstituteDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.InstituteService;
import com.DevSprint.voluntrix_backend.services.SponsorService;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Public", description = "Public operations accessible to all authenticated users")
public class PublicController {

    private final InstituteService instituteService;
    private final SponsorService sponsorService;

    @GetMapping("/institutes")
    @RequiresRole({UserType.VOLUNTEER, UserType.SPONSOR, UserType.ORGANIZATION, UserType.ADMIN, UserType.PUBLIC})
    public ResponseEntity<ApiResponse<List<InstituteDTO>>> getAllInstitutes() {
        List<InstituteDTO> institutes = instituteService.getAllInstitutes();
        return ResponseEntity.ok(new ApiResponse<>("Institutes retrieved successfully", institutes));
    }

    @GetMapping("/institutes/{key}")
    @RequiresRole({UserType.VOLUNTEER, UserType.SPONSOR, UserType.ORGANIZATION, UserType.ADMIN, UserType.PUBLIC})
    public ResponseEntity<ApiResponse<InstituteDTO>> getInstituteByKey(
            @Parameter(description = "Institute key") @PathVariable String key) {
        InstituteDTO institute = instituteService.getInstituteByKey(key);
        return ResponseEntity.ok(new ApiResponse<>("Institute retrieved successfully", institute));
    }

    @GetMapping("/sponsors/all")
    public ResponseEntity<ApiResponse<List<SponsorDTO>>> getAllSponsorsForAdmin() {
        List<SponsorDTO> sponsors = sponsorService.getAllSponsors();
        return ResponseEntity.ok(new ApiResponse<>("All sponsors retrieved successfully", sponsors));
    }
}
