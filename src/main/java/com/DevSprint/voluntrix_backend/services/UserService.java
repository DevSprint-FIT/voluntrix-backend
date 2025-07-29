package com.DevSprint.voluntrix_backend.services;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.AuthResponseDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.repositories.EmailVerificationRepository;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final VolunteerRepository volunteerRepository;
    private final SponsorRepository sponsorRepository;
    private final OrganizationRepository organizationRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JwtService jwtService;

    public AuthResponseDTO setUserRole(UserType role) {
        Long userId = currentUserService.getCurrentUserId();
        UserEntity user = currentUserService.getCurrentUser(userId);

        // Exception on role change attempt
        if (user.getRole() != null) {
            throw new IllegalStateException("Role is already set and cannot be changed");
        }

        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        user.setRole(role);
        userRepository.save(user);

        // Generate a new JWT token with the updated role
        String newToken = jwtService.generateToken(user);

        String nextStep = "COMPLETE_PROFILE";
        String redirectUrl = "/complete-profile";

        // Check if profile is already completed for this role
        boolean isProfileCompleted = false;
        if (role != null) {
            isProfileCompleted = switch (role) {
                case VOLUNTEER -> volunteerRepository.existsById(userId);
                case SPONSOR -> sponsorRepository.existsById(userId);
                case ORGANIZATION -> organizationRepository.existsById(userId);
                case ADMIN -> true;
                case PUBLIC -> {
                    nextStep = "DASHBOARD";
                    redirectUrl = "/dashboard";
                    yield true; 
                }
            };
        }

        if (isProfileCompleted) {
            nextStep = "DASHBOARD";
            redirectUrl = "/dashboard";
        }

        return AuthResponseDTO.builder()
            .token(newToken)
            .userId(user.getUserId())
            .email(user.getEmail())
            .handle(user.getHandle())
            .fullName(user.getFullName())
            .role(user.getRole().name())
            .isEmailVerified(user.getIsVerified())
            .isProfileCompleted(isProfileCompleted)
            .createdAt(user.getCreatedAt())
            .lastLogin(user.getLastLogin())
            .authProvider(user.getAuthProvider().name())
            .nextStep(nextStep)
            .redirectUrl(redirectUrl)
            .build();
    }

    // Delete user account and all associated role-specific data using JWT
    public void deleteAccount() {
        Long userId = currentUserService.getCurrentUserId();
        UserEntity user = currentUserService.getCurrentUser(userId);

        // Delete email verification records first
        emailVerificationRepository.deleteByUser(user.getUserId());

        // Delete role-specific data (foreign key constraints)
        if (user.getRole() != null) {
            switch (user.getRole()) {
                case VOLUNTEER:
                    volunteerRepository.findByUser(user).ifPresent(volunteer -> {
                        volunteerRepository.delete(volunteer);
                    });
                    break;
                case SPONSOR:
                    sponsorRepository.findById(userId).ifPresent(sponsor -> {
                        sponsorRepository.delete(sponsor);
                    });
                    break;
                case ORGANIZATION:
                    organizationRepository.findById(userId).ifPresent(organization -> {
                        organizationRepository.delete(organization);
                    });
                    break;
                case PUBLIC:
                    // No additional data to delete for PUBLIC users
                    break;
                case ADMIN:
                    break;
            }
        }

        userRepository.delete(user);
    }
}
