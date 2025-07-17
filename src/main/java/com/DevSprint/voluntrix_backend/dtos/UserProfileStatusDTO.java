package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileStatusDTO {
    private UserType role;
    private boolean isProfileCompleted;
}