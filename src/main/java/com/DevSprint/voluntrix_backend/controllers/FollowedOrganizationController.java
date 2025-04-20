package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.FollowOrganizationDTO;
import com.DevSprint.voluntrix_backend.services.FollowedOrganizationService;
import com.DevSprint.voluntrix_backend.dtos.MonthlyFollowCountDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/follow")
public class FollowedOrganizationController {

    @Autowired
    private FollowedOrganizationService followedOrganizationService;


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
        List<Long> organizationIds = followedOrganizationService.getFollowedOrganizations(volunteerId)
                .stream()
                .map(FollowOrganizationDTO::getOrganizationId)
                .collect(Collectors.toList());

        return ResponseEntity.ok(organizationIds);
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
