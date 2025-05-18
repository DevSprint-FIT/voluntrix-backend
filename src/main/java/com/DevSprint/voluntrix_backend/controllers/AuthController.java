package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.SignupRequestDto;
import com.DevSprint.voluntrix_backend.dtos.SignupResponseDto;
import com.DevSprint.voluntrix_backend.services.AuthService;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@RequestBody @Valid SignupRequestDto request) {
        return ResponseEntity.ok(authService.signUp(request));
    }
}
