package com.DevSprint.voluntrix_backend.services.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleBasedAccessService {

    private final CurrentUserService currentUserService;
    private final JwtService jwtService;
    private final HttpServletRequest request;

    // Extract role from JWT token for proper security validation
    private UserType extractRoleFromToken() {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return null;
            }

            String token = authHeader.substring(7);
            String jwtRole = jwtService.extractRole(token);
            
            // Convert JWT role format to UserType
            if (jwtRole != null && jwtRole.startsWith("ROLE_")) {
                String roleValue = jwtRole.substring(5); // Remove "ROLE_" prefix
                try {
                    return UserType.valueOf(roleValue);
                } catch (IllegalArgumentException e) {
                    // Handle ROLE_UNASSIGNED or invalid roles
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // Check if the current user has the specified role (validates JWT token)
    public boolean hasRole(UserType requiredRole) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }

            UserType tokenRole = extractRoleFromToken();
            return tokenRole == requiredRole;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if the current user has any of the specified roles (validates JWT token)
    public boolean hasAnyRole(UserType... roles) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }

            UserType tokenRole = extractRoleFromToken();
            if (tokenRole == null) {
                return false;
            }

            for (UserType role : roles) {
                if (tokenRole == role) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if the current user has a role assigned (validates JWT token)
    public boolean hasAssignedRole() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }

            return extractRoleFromToken() != null;
        } catch (Exception e) {
            return false;
        }
    }

    // Get the current user's role from JWT token
    public UserType getCurrentUserRole() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }

            return extractRoleFromToken();
        } catch (Exception e) {
            return null;
        }
    }

    // Get the current user's role from database (for display purposes only)
    public UserType getCurrentUserRoleFromDatabase() {
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
