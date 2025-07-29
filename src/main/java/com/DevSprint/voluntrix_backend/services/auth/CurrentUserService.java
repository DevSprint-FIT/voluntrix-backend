package com.DevSprint.voluntrix_backend.services.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.exceptions.UserNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CurrentUserService {
    
    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final SponsorRepository sponsorRepository;
    private final OrganizationRepository organizationRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User not found"))
                    .getUserId();
        }
        throw new RuntimeException("Unauthorized");
    }

    public UserEntity getCurrentUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
                return user;
    }

    /*
     * Get the current user's role-specific entity ID
     * The entity ID (volunteerId, sponsorId, organizationId, or userId for admin), or null if not found
     */
    public Long getCurrentEntityId() {
        Long userId = getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() == null) {
            return null;
        }

        return switch (user.getRole()) {
            case VOLUNTEER -> volunteerRepository.findByUser(user)
                .map(volunteer -> volunteer.getVolunteerId())
                .orElse(null);
            case SPONSOR -> sponsorRepository.findByUser(user)
                .map(sponsor -> sponsor.getSponsorId())
                .orElse(null);
            case ORGANIZATION -> organizationRepository.findByUser(user)
                .map(organization -> organization.getId())
                .orElse(null);
            case ADMIN -> user.getUserId();
            case PUBLIC -> null;
        };
    }

    public UserType getCurrentUserType() {
        Long userId = getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        if (user.getRole() == null) {
            throw new RuntimeException("User role is not set");
        }
        
        return user.getRole();
    }

}
