package com.DevSprint.voluntrix_backend.services;

import java.util.regex.Pattern;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.AuthResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.EmailVerificationResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.LoginRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.SignupRequestDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.exceptions.UserNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.utils.UserMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final VolunteerRepository volunteerRepository;
    private final SponsorRepository sponsorRepository;
    private final OrganizationRepository organizationRepository;
    private final EmailVerificationService emailVerificationService;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public ApiResponse<AuthResponseDTO> signUp(SignupRequestDTO request) {
        // Additional server-side email validation
        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        if(userRepository.existsByHandle(request.getHandle())) {
            throw new IllegalArgumentException("Handle already taken.");
        }

        UserEntity user = userMapper.toEntity(request);
        userRepository.save(user);

        // Send verification email
        emailVerificationService.sendVerificationEmail(user);

        UserDetails userDetails = userMapper.toUserDetails(user);
        String token = jwtService.generateToken(userDetails);

        // Determine next step for the user
        String nextStep = user.getIsVerified() ? 
            (user.getRole() == null ? "SELECT_ROLE" : "COMPLETE_PROFILE") : 
            "VERIFY_EMAIL";

        String redirectUrl = switch (nextStep) {
            case "VERIFY_EMAIL" -> "/verify-email";
            case "SELECT_ROLE" -> "/select-role";
            case "COMPLETE_PROFILE" -> "/complete-profile";
            default -> "/dashboard";
        };

        AuthResponseDTO authResponse = AuthResponseDTO.builder()
            .token(token)
            .userId(user.getUserId())
            .email(user.getEmail())
            .handle(user.getHandle())
            .fullName(user.getFullName())
            .role(user.getRole() != null ? user.getRole().name() : null)
            .isEmailVerified(user.getIsVerified())
            .isProfileCompleted(user.getIsProfileCompleted())
            .createdAt(user.getCreatedAt())
            .lastLogin(user.getLastLogin())
            .authProvider(user.getAuthProvider().name())
            .nextStep(nextStep)
            .redirectUrl(redirectUrl)
            .build();

        return new ApiResponse<>(
            "User registered successfully. Please check your email for verification code.",
            authResponse
        );
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid email or password");
        }

        // Update last login timestamp
        user.setLastLogin(java.time.LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        boolean isProfileCompleted = false;
        if (user.getRole() != null) {
            isProfileCompleted = switch (user.getRole()) {
                case VOLUNTEER -> volunteerRepository.existsById(user.getUserId());
                case SPONSOR -> sponsorRepository.existsById(user.getUserId());
                case ORGANIZATION -> organizationRepository.existsById(user.getUserId());
                case PUBLIC -> false;
            };
        }

        // Determine next step for the user
        String nextStep;
        if (!user.getIsVerified()) {
            nextStep = "VERIFY_EMAIL";
        } else if (user.getRole() == null) {
            nextStep = "SELECT_ROLE";
        } else if (!isProfileCompleted) {
            nextStep = "COMPLETE_PROFILE";
        } else {
            nextStep = "DASHBOARD";
        }

        String redirectUrl = switch (nextStep) {
            case "VERIFY_EMAIL" -> "/verify-email";
            case "SELECT_ROLE" -> "/select-role";
            case "COMPLETE_PROFILE" -> "/complete-profile";
            default -> "/dashboard";
        };

        return AuthResponseDTO.builder()
            .token(token)
            .userId(user.getUserId())
            .email(user.getEmail())
            .handle(user.getHandle())
            .fullName(user.getFullName())
            .role(user.getRole() != null ? user.getRole().name() : null)
            .isEmailVerified(user.getIsVerified())
            .isProfileCompleted(isProfileCompleted)
            .createdAt(user.getCreatedAt())
            .lastLogin(user.getLastLogin())
            .authProvider(user.getAuthProvider().name())
            .nextStep(nextStep)
            .redirectUrl(redirectUrl)
            .build();
    }

    public ApiResponse<EmailVerificationResponseDTO> verifyEmail(Long userId, String otp) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean isVerified = emailVerificationService.verifyOTP(user, otp);
        
        if (isVerified) {
            return new ApiResponse<>(
                "Email verified successfully",
                new EmailVerificationResponseDTO(true, "Email verified successfully")
            );
        } else {
            return new ApiResponse<>(
                "Invalid or expired OTP",
                new EmailVerificationResponseDTO(false, "Invalid or expired OTP")
            );
        }
    }

    public ApiResponse<String> resendVerificationEmail(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getIsVerified()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        emailVerificationService.sendVerificationEmail(user);
        
        return new ApiResponse<>(
            "Verification email sent successfully",
            "Please check your email for the new verification code"
        );
    }

    // Email-based verification methods (no authentication required)
    public ApiResponse<EmailVerificationResponseDTO> verifyEmailWithEmailAndOtp(String email, String otp) {
        UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        boolean isVerified = emailVerificationService.verifyOTP(user, otp);
        
        if (isVerified) {
            return new ApiResponse<>(
                "Email verified successfully",
                new EmailVerificationResponseDTO(true, "Email verified successfully")
            );
        } else {
            return new ApiResponse<>(
                "Invalid or expired OTP",
                new EmailVerificationResponseDTO(false, "Invalid or expired OTP")
            );
        }
    }

    public ApiResponse<String> resendVerificationEmailByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (user.getIsVerified()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        emailVerificationService.sendVerificationEmail(user);
        
        return new ApiResponse<>(
            "Verification email sent successfully",
            "Please check your email for the new verification code"
        );
    }
}
