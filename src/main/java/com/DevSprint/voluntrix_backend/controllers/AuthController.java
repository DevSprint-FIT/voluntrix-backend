package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.AuthResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.CurrentUserDTO;
import com.DevSprint.voluntrix_backend.dtos.EmailVerificationResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.LoginRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.RefreshTokenRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.RefreshTokenResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.ResendVerificationRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.SignupRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.UserProfileStatusDTO;
import com.DevSprint.voluntrix_backend.dtos.VerifyEmailRequestDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.services.AuthService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/auth")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    private final AuthService authService;
    private final CurrentUserService currentUserService;
    
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> signup(@RequestBody @Valid SignupRequestDTO request) {
        ApiResponse<AuthResponseDTO> response = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CurrentUserDTO>> getCurrentUser() {
        Long userId = currentUserService.getCurrentUserId();
        CurrentUserDTO currentUser = authService.getCurrentUser(userId);
        return ResponseEntity.ok(new ApiResponse<>("Current user retrieved successfully", currentUser));
    }

    @GetMapping("/profile-status")
    public ResponseEntity<UserProfileStatusDTO> getUserProfileStatus() {
        Long userId = currentUserService.getCurrentUserId();
        UserEntity user = currentUserService.getCurrentUser(userId);

        return ResponseEntity.ok(new UserProfileStatusDTO(user.getRole(), user.getIsProfileCompleted()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@RequestBody @Valid LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<AuthResponseDTO>("Login successful", response));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<EmailVerificationResponseDTO>> verifyEmail(@RequestBody @Valid VerifyEmailRequestDTO request) {
        ApiResponse<EmailVerificationResponseDTO> response = authService.verifyEmailWithEmailAndOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerificationEmail(@RequestBody @Valid ResendVerificationRequestDTO request) {
        ApiResponse<String> response = authService.resendVerificationEmailByEmail(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponseDTO>> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO request) {
        RefreshTokenResponseDTO response = authService.refreshToken(request);
        return ResponseEntity.ok(new ApiResponse<>("Token refreshed successfully", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody(required = false) RefreshTokenRequestDTO request) {
        String refreshToken = request != null ? request.getRefreshToken() : null;
        authService.logout(refreshToken);
        return ResponseEntity.ok(new ApiResponse<>("Logout successful", "You have been logged out successfully."));
    }
}
