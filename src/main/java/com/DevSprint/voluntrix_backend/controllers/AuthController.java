package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.AuthResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.EmailVerificationResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.LoginRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.SignupRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.UserProfileStatusDTO;
import com.DevSprint.voluntrix_backend.dtos.VerifyEmailRequestDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.services.AuthService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CurrentUserService currentUserService;
    
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> signup(@RequestBody @Valid SignupRequestDTO request) {
        return ResponseEntity.ok(authService.signUp(request));
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

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(new ApiResponse<>("Logout successful", "You have been logged out successfully."));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<EmailVerificationResponseDTO>> verifyEmail(@RequestBody @Valid VerifyEmailRequestDTO request) {
        Long userId = currentUserService.getCurrentUserId();
        return ResponseEntity.ok(authService.verifyEmail(userId, request.getOtp()));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<String>> resendVerificationEmail() {
        Long userId = currentUserService.getCurrentUserId();
        return ResponseEntity.ok(authService.resendVerificationEmail(userId));
    }
}
