package com.DevSprint.voluntrix_backend.services.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleBasedAccessService {

    private final CurrentUserService currentUserService;

    // Check if the current user has the specified role
    public boolean hasRole(UserType requiredRole) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }

            Long userId = currentUserService.getCurrentUserId();
            UserEntity user = currentUserService.getCurrentUser(userId);
            
            return user.getRole() == requiredRole;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if the current user has any of the specified roles
    public boolean hasAnyRole(UserType... roles) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }

            Long userId = currentUserService.getCurrentUserId();
            UserEntity user = currentUserService.getCurrentUser(userId);
            
            UserType userRole = user.getRole();
            for (UserType role : roles) {
                if (userRole == role) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if the current user has a role assigned
    public boolean hasAssignedRole() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }

            Long userId = currentUserService.getCurrentUserId();
            UserEntity user = currentUserService.getCurrentUser(userId);
            
            return user.getRole() != null;
        } catch (Exception e) {
            return false;
        }
    }

    // Get the current user's role
    public UserType getCurrentUserRole() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }

            Long userId = currentUserService.getCurrentUserId();
            UserEntity user = currentUserService.getCurrentUser(userId);
            
            return user.getRole();
        } catch (Exception e) {
            return null;
        }
    }
}
