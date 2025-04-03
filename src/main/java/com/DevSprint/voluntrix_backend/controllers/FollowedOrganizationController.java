package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.services.FollowedOrganizationService;
import com.DevSprint.voluntrix_backend.dtos.FollowOrganizationDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/follow")
public class FollowedOrganizationController {

    @Autowired
    private FollowedOrganizationService followedOrganizationService;

    
    @PostMapping("/")
    public String followOrganization(@RequestBody FollowOrganizationDTO request) {
        return followedOrganizationService.followOrganization(request.getVolunteerId(), request.getOrganizationId());
    }

    
    @DeleteMapping("/{volunteerId}/{organizationId}")
    public String unfollowOrganization(@PathVariable Long volunteerId, @PathVariable Long organizationId) {
        return followedOrganizationService.unfollowOrganization(volunteerId, organizationId);
    }

    
    @GetMapping("/{volunteerId}")
    public List<Long> getFollowedOrganizations(@PathVariable Long volunteerId) {
       return followedOrganizationService.getFollowedOrganizations(volunteerId)
                                      .stream()
                                      .map(FollowOrganizationDTO::getOrganizationId) 
                                      .collect(Collectors.toList());
    }
}