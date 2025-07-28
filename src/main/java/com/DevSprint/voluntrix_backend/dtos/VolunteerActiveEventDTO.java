package com.DevSprint.voluntrix_backend.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerActiveEventDTO {
    private Long eventId;
    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
}
