package com.DevSprint.voluntrix_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLeaderboardDTO {
    private String firstName;
    private String lastName;
    private Integer eventRewardPoints;
    private String profilePictureUrl;
}