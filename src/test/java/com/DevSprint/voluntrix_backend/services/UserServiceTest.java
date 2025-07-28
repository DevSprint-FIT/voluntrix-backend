package com.DevSprint.voluntrix_backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.DevSprint.voluntrix_backend.dtos.AuthResponseDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.AuthProvider;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CurrentUserService currentUserService;
    @Mock
    private VolunteerRepository volunteerRepository;
    @Mock
    private SponsorRepository sponsorRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    private UserEntity testUser;
    private final Long TEST_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setUserId(TEST_USER_ID);
        testUser.setEmail("harindu@test.com");
        testUser.setHandle("harindu");
        testUser.setFullName("Harindu Hadithya");
        testUser.setRole(null);
        testUser.setIsVerified(true);
        testUser.setAuthProvider(AuthProvider.EMAIL);
    }

    @Test
    void setUserRole_ShouldSetRoleSuccessfully() {
        /*
            Purpose: This test verifies that the setUserRole method correctly sets the user's role
                    and returns the expected AuthResponseDTO with the new JWT token and role information.
        */


        // Given
        when(currentUserService.getCurrentUserId()).thenReturn(TEST_USER_ID);
        when(currentUserService.getCurrentUser(TEST_USER_ID)).thenReturn(testUser);
        when(volunteerRepository.existsById(TEST_USER_ID)).thenReturn(false);
        when(jwtService.generateToken(testUser)).thenReturn("new-jwt-token");

        // When
        AuthResponseDTO response = userService.setUserRole(UserType.VOLUNTEER);

        // Then
        assertNotNull(response);
        assertEquals("new-jwt-token", response.getToken());
        assertEquals(UserType.VOLUNTEER.name(), response.getRole());
        assertEquals("COMPLETE_PROFILE", response.getNextStep());
        assertEquals("/complete-profile", response.getRedirectUrl());
        assertFalse(response.isProfileCompleted());
        
        verify(userRepository).save(testUser);
        assertEquals(UserType.VOLUNTEER, testUser.getRole());
    }

    @Test
    void setUserRole_ShouldThrowException_WhenRoleAlreadySet() {
        /*
            Purpose: This test verifies that the setUserRole method throws an exception
                    when the user's role is already set, preventing any changes to the role.
        */

        // Given
        testUser.setRole(UserType.VOLUNTEER);
        when(currentUserService.getCurrentUserId()).thenReturn(TEST_USER_ID);
        when(currentUserService.getCurrentUser(TEST_USER_ID)).thenReturn(testUser);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.setUserRole(UserType.SPONSOR);
        });

        assertEquals("Role is already set and cannot be changed", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void setUserRole_ShouldThrowException_WhenRoleIsNull() {
        /*
            Purpose: This test verifies that the setUserRole method throws an exception
                    when the provided role is null, ensuring that the method does not accept null roles.
        */
        
        // Given
        when(currentUserService.getCurrentUserId()).thenReturn(TEST_USER_ID);
        when(currentUserService.getCurrentUser(TEST_USER_ID)).thenReturn(testUser);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.setUserRole(null);
        });

        assertEquals("Role cannot be null", exception.getMessage());
        verify(userRepository, never()).save(any());
    }


}


