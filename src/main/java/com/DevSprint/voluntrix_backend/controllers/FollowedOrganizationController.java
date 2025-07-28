package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.FollowOrganizationDTO;
import com.DevSprint.voluntrix_backend.services.FollowedOrganizationService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.UserType;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import com.DevSprint.voluntrix_backend.dtos.MonthlyFollowCountDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/follow")
public class FollowedOrganizationController {

    private final FollowedOrganizationService followedOrganizationService;
    private final CurrentUserService currentUserService;

    @PostMapping("/")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<String> followOrganization(@RequestBody FollowOrganizationDTO request) {
        Long volunteerId = currentUserService.getCurrentEntityId();
        String response = followedOrganizationService.followOrganization(volunteerId, request.getOrganizationId());
        return ResponseEntity.ok(response);
    }

    // Here also Volunteer ID can get from JWT
    // Unfollow an organization
    @DeleteMapping("/{organizationId}")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<String> unfollowOrganization(@PathVariable Long organizationId) {
        Long volunteerId = currentUserService.getCurrentEntityId();
        String response = followedOrganizationService.unfollowOrganization(volunteerId, organizationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/followed-organizations")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<List<Long>> getFollowedOrganizations() {
        Long volunteerId = currentUserService.getCurrentEntityId();
        List<Long> organizationNames = followedOrganizationService.getFollowedOrganizations(volunteerId);
        return ResponseEntity.ok(organizationNames);
    }

    //Monthly follower statistics
    @GetMapping("/stats")
    @RequiresRole(UserType.ORGANIZATION)
    public ResponseEntity<List<MonthlyFollowCountDTO>> getMonthlyFollowerStats(
            @RequestParam int year){
        Long organizationId = currentUserService.getCurrentEntityId();
        List<MonthlyFollowCountDTO> stats = followedOrganizationService.getMonthlyFollowerStats(year, organizationId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/institute-distribution")
    @RequiresRole(UserType.ORGANIZATION)
    public ResponseEntity<Map<String, Long>> getInstituteDistribution() {
        Long organizationId = currentUserService.getCurrentEntityId();
        Map<String, Long> data = followedOrganizationService.getInstituteDistributionByOrganization(organizationId);
        return ResponseEntity.ok(data);
    }

}
