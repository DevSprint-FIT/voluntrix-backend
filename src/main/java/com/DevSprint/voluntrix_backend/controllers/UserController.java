package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.AuthResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.SetRoleRequestDTO;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.UserService;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @PatchMapping("/set-role")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> setRole(@RequestBody @Valid SetRoleRequestDTO request) {
        AuthResponseDTO response = userService.setUserRole(request.getRole());
        return ResponseEntity.ok(new ApiResponse<>("Role set successfully", response));
    }

    @DeleteMapping("/account")
    @RequiresRole({UserType.VOLUNTEER, UserType.SPONSOR, UserType.ORGANIZATION})
    public ResponseEntity<ApiResponse<String>> deleteAccount() {
        userService.deleteAccount();
        return ResponseEntity.ok(new ApiResponse<>(
            "Account deleted successfully",
            "Your account and all associated data have been permanently deleted"
        ));
    }
}
