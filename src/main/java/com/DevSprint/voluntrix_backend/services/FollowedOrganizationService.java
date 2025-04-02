package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.FollowedOrganization;
import com.DevSprint.voluntrix_backend.entities.Organization;
import com.DevSprint.voluntrix_backend.repositories.FollowedOrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowedOrganizationService {

    @Autowired
    private FollowedOrganizationRepository followedOrganizationRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    // Follow an organization and update follow count
    @Transactional
    public String followOrganization(Long volunteerId, Long organizationId) {
        FollowedOrganization followedOrganization = new FollowedOrganization(volunteerId, organizationId);
        followedOrganizationRepository.save(followedOrganization);

        // Update follower count in organization table
        Organization organization = organizationRepository.findById(organizationId).orElse(null);
        if (organization != null) {
            organization.setFollowerCount(organization.getFollowerCount() + 1);
            organizationRepository.save(organization);
        }

        return "Organization followed successfully!";
    }

    // Unfollow an organization and update follower count
    @Transactional
    public String unfollowOrganization(Long volunteerId, Long organizationId) {
        followedOrganizationRepository.deleteByVolunteerIdAndOrganizationId(volunteerId, organizationId);

        // Update follower count in organization table
        Organization organization = organizationRepository.findById(organizationId).orElse(null);
        if (organization != null && organization.getFollowerCount() > 0) {
            organization.setFollowerCount(organization.getFollowerCount() - 1);
            organizationRepository.save(organization);
        }

        return "Organization unfollowed successfully!";
    }

    // Get all followed organizations for a volunteer
    public List<Long> getFollowedOrganizations(Long volunteerId) {
        return followedOrganizationRepository.findByVolunteerId(volunteerId)
                .stream()
                .map(FollowedOrganization::getOrganizationId)
                .collect(Collectors.toList());
    }
}
