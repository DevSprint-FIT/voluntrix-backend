package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.UserType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetRoleRequestDTO {
    @NotNull(message = "Role is required")
    private UserType role;
}
