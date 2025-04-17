package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.FollowedOrganization;
import com.DevSprint.voluntrix_backend.entities.Organization;
import com.DevSprint.voluntrix_backend.exceptions.OrganizationNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerAlreadyFollowsOrganizationException;
import com.DevSprint.voluntrix_backend.repositories.FollowedOrganizationRepository;
import com.DevSprint.voluntrix_backend.utils.EntityDTOConverter;
import com.DevSprint.voluntrix_backend.dtos.FollowOrganizationDTO;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowedOrganizationService {

    @Autowired
    private FollowedOrganizationRepository followedOrganizationRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EntityDTOConverter entityDTOConverter;

    // Follow an organization and update follow count
    @Transactional
    public String followOrganization(Long volunteerId, Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + organizationId));

        boolean alreadyFollowing = followedOrganizationRepository.findByVolunteerId(volunteerId).stream()
                .anyMatch(f -> f.getOrganizationId().equals(organizationId));

        if (alreadyFollowing) {
            throw new VolunteerAlreadyFollowsOrganizationException("Volunteer already follows this organization.");
        }

        FollowedOrganization followedOrganization = new FollowedOrganization();
        followedOrganization.setVolunteerId(volunteerId);
        followedOrganization.setOrganizationId(organizationId);
        followedOrganizationRepository.save(followedOrganization);

        organization.setFollowerCount(organization.getFollowerCount() + 1);
        organizationRepository.save(organization);

        return "Organization followed successfully!";
    }

    // Unfollow an organization and update follower count
    @Transactional
    public String unfollowOrganization(Long volunteerId, Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + organizationId));

        Optional<FollowedOrganization> followOptional = followedOrganizationRepository.findByVolunteerId(volunteerId).stream()
                .filter(f -> f.getOrganizationId().equals(organizationId))
                .findFirst();

        if (followOptional.isEmpty()) {
            return "Volunteer was not following this organization.";
        }

        followedOrganizationRepository.delete(followOptional.get());

        if (organization.getFollowerCount() > 0) {
            organization.setFollowerCount(organization.getFollowerCount() - 1);
            organizationRepository.save(organization);
        }

        return "Organization unfollowed successfully!";
    }

    // Get all followed organizations for a volunteer and map to DTO
    public List<FollowOrganizationDTO> getFollowedOrganizations(Long volunteerId) {
        return followedOrganizationRepository.findByVolunteerId(volunteerId)
                .stream()
                .map(entityDTOConverter::toFollowOrganizationDTO)
                .collect(Collectors.toList());
    }
}
