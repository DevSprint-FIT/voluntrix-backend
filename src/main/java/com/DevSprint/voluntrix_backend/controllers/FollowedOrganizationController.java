package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.FollowOrganizationDTO;
import com.DevSprint.voluntrix_backend.services.FollowedOrganizationService;

import lombok.RequiredArgsConstructor;

import com.DevSprint.voluntrix_backend.dtos.MonthlyFollowCountDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/follow")
public class FollowedOrganizationController {

    private final FollowedOrganizationService followedOrganizationService;

    @PostMapping("/")
    public ResponseEntity<String> followOrganization(@RequestBody FollowOrganizationDTO request) {
        String response = followedOrganizationService.followOrganization(request.getVolunteerId(), request.getOrganizationId());
        return ResponseEntity.ok(response);
    }

    // Unfollow an organization
    @DeleteMapping("/{volunteerId}/{organizationId}")
    public ResponseEntity<String> unfollowOrganization(@PathVariable Long volunteerId, @PathVariable Long organizationId) {
        String response = followedOrganizationService.unfollowOrganization(volunteerId, organizationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{volunteerId}")
    public ResponseEntity<List<Long>> getFollowedOrganizations(@PathVariable Long volunteerId) {
        List<Long> organizationNames = followedOrganizationService.getFollowedOrganizations(volunteerId);
        return ResponseEntity.ok(organizationNames);
    }

    //Monthly follower statistics
    @GetMapping("/stats/{organizationId}")
    public ResponseEntity<List<MonthlyFollowCountDTO>> getMonthlyFollowerStats(
            @PathVariable Long organizationId,
            @RequestParam int year){
        List<MonthlyFollowCountDTO> stats = followedOrganizationService.getMonthlyFollowerStats(year, organizationId);
        return ResponseEntity.ok(stats);
    }
}
