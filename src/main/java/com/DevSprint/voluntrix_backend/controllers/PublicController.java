package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.dtos.InstituteDTO;
import com.DevSprint.voluntrix_backend.dtos.OrganizationDTO;
import com.DevSprint.voluntrix_backend.dtos.SocialFeedResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.EventService;
import com.DevSprint.voluntrix_backend.services.InstituteService;
import com.DevSprint.voluntrix_backend.services.OrganizationService;
import com.DevSprint.voluntrix_backend.services.SocialFeedService;
import com.DevSprint.voluntrix_backend.services.SponsorService;
import com.DevSprint.voluntrix_backend.services.VolunteerService;
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
    private final VolunteerService volunteerService;
    private final EventService eventService;
    private final SocialFeedService socialFeedService;
    private final OrganizationService organizationService;

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
    @RequiresRole({UserType.VOLUNTEER, UserType.ADMIN, UserType.ORGANIZATION, UserType.SPONSOR})
    public ResponseEntity<ApiResponse<List<SponsorDTO>>> getAllSponsorsForAdmin() {
        List<SponsorDTO> sponsors = sponsorService.getAllSponsors();
        return ResponseEntity.ok(new ApiResponse<>("All sponsors retrieved successfully", sponsors));
    }

    @GetMapping("/volunteers/all")
    @RequiresRole({UserType.VOLUNTEER, UserType.ADMIN, UserType.ORGANIZATION, UserType.SPONSOR})
    public ResponseEntity<List<VolunteerDTO>> getAllVolunteers() {
        List<VolunteerDTO> volunteers = volunteerService.getAllVolunteers();
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/events/all")
    @RequiresRole({UserType.VOLUNTEER, UserType.ADMIN, UserType.ORGANIZATION, UserType.SPONSOR})
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return new ResponseEntity<List<EventDTO>>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/posts/all")
    @RequiresRole({UserType.VOLUNTEER, UserType.ADMIN, UserType.ORGANIZATION, UserType.SPONSOR})
    public ResponseEntity<List<SocialFeedResponseDTO>> getAllPosts(){
        List<SocialFeedResponseDTO> posts = socialFeedService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/organizations/{id}")
    @RequiresRole({UserType.VOLUNTEER, UserType.ADMIN, UserType.ORGANIZATION, UserType.SPONSOR})
    public ResponseEntity<ApiResponse<OrganizationDTO>> getOrganizationById(
            @Parameter(description = "Organization ID") @PathVariable Long id) {
        OrganizationDTO organization = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(new ApiResponse<>("Organization retrieved successfully", organization));
    }
}