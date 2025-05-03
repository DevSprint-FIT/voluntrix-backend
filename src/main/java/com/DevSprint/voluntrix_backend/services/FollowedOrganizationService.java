package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.FollowedOrganizationEntity;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.exceptions.OrganizationNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerAlreadyFollowsOrganizationException;
import com.DevSprint.voluntrix_backend.repositories.FollowedOrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.utils.FollowSystemDTOConverter;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.DevSprint.voluntrix_backend.dtos.MonthlyFollowCountDTO;


import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowedOrganizationService {

    @Autowired
    private FollowedOrganizationRepository followedOrganizationRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private FollowSystemDTOConverter entityDTOConverter;

    @Autowired
    private VolunteerRepository volunteerRepository;


    // Follow an organization and update follow count
    @Transactional
    public String followOrganization(Long volunteerId, Long organizationId) {

        if (!volunteerRepository.existsById(volunteerId)) {
            throw new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId);
        }

        OrganizationEntity organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + organizationId));

        boolean alreadyFollowing = followedOrganizationRepository.findByVolunteerId(volunteerId).stream()
                .anyMatch(f -> f.getOrganizationId().equals(organizationId));

        if (alreadyFollowing) {
            throw new VolunteerAlreadyFollowsOrganizationException("Volunteer already follows this organization.");
        }

        FollowedOrganizationEntity followedOrganization = new FollowedOrganizationEntity();
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

        if (!volunteerRepository.existsById(volunteerId)) {
            throw new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId);
        }

        OrganizationEntity organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + organizationId));

        Optional<FollowedOrganizationEntity> followOptional = followedOrganizationRepository.findByVolunteerId(volunteerId).stream()
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
    public List<String> getFollowedOrganizations(Long volunteerId) {
        if (!volunteerRepository.existsById(volunteerId)) {
            throw new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId);
        }

        return followedOrganizationRepository.findByVolunteerId(volunteerId)
                .stream()
                .map(f -> {
                    OrganizationEntity org = organizationRepository.findById(f.getOrganizationId()).orElse(null);
                    return org != null ? org.getName() : null;
                })
                .filter(name -> name != null)
                .collect(Collectors.toList());
    }


    public List<MonthlyFollowCountDTO> getMonthlyFollowerStats(int year, Long organizationId) {
        // Validate organization exists
        OrganizationEntity organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + organizationId));

        List<Object[]> rawStats = followedOrganizationRepository.countMonthlyFollowers(year, organizationId);

        return rawStats.stream()
                .map(obj -> {
                    Integer monthNumber = (Integer) obj[0];
                    Long count = (Long) obj[1];
                    String monthName = Month.of(monthNumber).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                    return new MonthlyFollowCountDTO(monthName, count);
                })
                .collect(Collectors.toList());
    }

}
