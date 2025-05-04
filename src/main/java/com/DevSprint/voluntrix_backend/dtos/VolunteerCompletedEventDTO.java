package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.EventType;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerCompletedEventDTO {
    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;
    private EventType eventType;
    private String contributionArea;
}
