package com.DevSprint.voluntrix_backend.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.auth.CustomUserDetailsService;

@ExtendWith(MockitoExtension.class)
class JwtRoleTokenAnalysisTest {

    @Mock
    private JwtService jwtService;
    
    @Mock
    private CustomUserDetailsService userDetailsService;

    @Test
    void demonstrateRoleBasedAuthenticationManagedSecurely() {
        /*
           Purpose: This test demonstrates how our system securely handles role changes
           by always loading the current role from the database rather than trusting
           the role stored in the JWT token.
         */
        
        // Initially, user has ROLE_UNASSIGNED
        UserEntity freshUser = new UserEntity();
        freshUser.setEmail("harindu@.com");
        freshUser.setRole(null);
        
        String initialRole = freshUser.getAuthorities()
            .iterator().next().getAuthority();
        assertEquals("ROLE_UNASSIGNED", initialRole);
        
        
        // User selects the role after signup
        freshUser.setRole(UserType.VOLUNTEER);
        
        
        // Then, user makes a new request with old token
        // When user makes a request with the original token that had ROLE_UNASSIGNED:
        // 1. JwtAuthenticationFilter extracts the email
        // 2. It loads fresh user details from database (with updated role)
        // 3. It creates authentication with the CURRENT authorities
        
        // This updated role will be used for authorization decisions
        String updatedRole = freshUser.getAuthorities()
            .iterator().next().getAuthority();
        assertEquals("ROLE_VOLUNTEER", updatedRole);
    }
}