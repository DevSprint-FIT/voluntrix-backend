package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.AuthResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.SetRoleRequestDTO;
import com.DevSprint.voluntrix_backend.services.UserService;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("/set-role")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> setRole(@RequestBody @Valid SetRoleRequestDTO request) {
        AuthResponseDTO response = userService.setUserRole(request.getRole());
        return ResponseEntity.ok(new ApiResponse<>("Role set successfully", response));
    }
}
