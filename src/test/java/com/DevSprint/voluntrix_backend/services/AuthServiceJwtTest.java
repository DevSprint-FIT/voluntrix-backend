package com.DevSprint.voluntrix_backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.DevSprint.voluntrix_backend.dtos.AuthResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.SignupRequestDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.AuthProvider;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.utils.UserMapper;

@ExtendWith(MockitoExtension.class)
class AuthServiceJwtTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VolunteerRepository volunteerRepository;
    @Mock
    private SponsorRepository sponsorRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private EmailVerificationService emailVerificationService;

    @InjectMocks
    private AuthService authService;

    private SignupRequestDTO signupRequest;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequestDTO();
        signupRequest.setEmail("harinu@example.com");
        signupRequest.setHandle("harindu");
        signupRequest.setFullName("Harindu Hadithya");
        signupRequest.setPassword("123456789");

        // Initialize a test user with default values
        testUser = new UserEntity();
        testUser.setUserId(1L);
        testUser.setEmail("harinu@example.com");
        testUser.setHandle("harindu");
        testUser.setFullName("Harindu Hadithya");
        testUser.setRole(null); 
        testUser.setIsVerified(false);
        testUser.setAuthProvider(AuthProvider.EMAIL);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setIsProfileCompleted(false);
    }

    @Test
    void signup_ShouldGenerateTokenWithUnassignedRole() {
        /*
           Purpose: Tests whether a JWT token is generated when a new user signs up, and the user's role is unassigned.
        */

        // Given - user does not exist in the DB
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByHandle(signupRequest.getHandle())).thenReturn(false);
        when(userMapper.toEntity(signupRequest)).thenReturn(testUser);
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);
        when(userMapper.toUserDetails(testUser)).thenReturn(testUser);
        when(jwtService.generateToken(testUser)).thenReturn("jwt-token-with-unassigned-role");

        // When - user signs up
        ApiResponse<AuthResponseDTO> response = authService.signUp(signupRequest);

        // Then - expect a successful response with JWT token
        assertNotNull(response);
        assertEquals("jwt-token-with-unassigned-role", response.getData().getToken());
        assertNull(response.getData().getRole());
        assertEquals("VERIFY_EMAIL", response.getData().getNextStep());

        // check behavior of mocks  
        verify(jwtService).generateToken(testUser);
    }

    @Test
    void jwtService_ShouldContainUnassignedRoleForNewUser() {
        /*
              Purpose: Tests whether the JWT service correctly assigns the "ROLE_UNASSIGNED" authority to a new user
              who has not yet set a role.
        */

        // Given - user has no role set
        testUser.setRole(null);
        
        // When - get authorities of the user
        var authorities = testUser.getAuthorities();
        
        // Then - expect the authority to be ROLE_UNASSIGNED
        assertEquals(1, authorities.size());
        assertEquals("ROLE_UNASSIGNED", authorities.iterator().next().getAuthority());
    }

    @Test
    void jwtService_ShouldContainVolunteerRoleAfterRoleSet() {
        /*
              Purpose: Tests whether the JWT service correctly assigns the "ROLE_VOLUNTEER" authority to a user
              after they have set their role to VOLUNTEER.
        */

        // Given - user set the role to VOLUNTEER
        testUser.setRole(UserType.VOLUNTEER);
        
        // When - get authorities of the user
        var authorities = testUser.getAuthorities();
        
        // Then - expect the authority to be ROLE_VOLUNTEER
        assertEquals(1, authorities.size());
        assertEquals("ROLE_VOLUNTEER", authorities.iterator().next().getAuthority());
    }
}

