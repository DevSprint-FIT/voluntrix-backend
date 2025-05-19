package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.LoginRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.SignupRequestDto;
import com.DevSprint.voluntrix_backend.dtos.SignupResponseDto;
import com.DevSprint.voluntrix_backend.dtos.UserProfileStatusDto;
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
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@RequestBody @Valid SignupRequestDto request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @GetMapping("/profile-status")
    public ResponseEntity<UserProfileStatusDto> getUserProfileStatus() {
        Long userId = currentUserService.getCurrentUserId();
        UserEntity user = currentUserService.getCurrentUser(userId);

        return ResponseEntity.ok(new UserProfileStatusDto(user.getRole(), user.getIsProfileCompleted()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<SignupResponseDto>> login(@RequestBody @Valid LoginRequestDTO request) {
        SignupResponseDto response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<SignupResponseDto>("Login successful", response));
    }
}
