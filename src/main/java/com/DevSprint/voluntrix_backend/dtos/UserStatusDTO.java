package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusDTO {
    private String userId;
    private String userName;
    private UserStatus status;
    private String lastSeen;

    public enum UserStatus {
        ONLINE,
        OFFLINE,
        TYPING
    }
}
