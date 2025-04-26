package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long eventId;
    private String eventTitle;
    private LocalDate eventDate;
    private String eventLocation;
    private EventStatus eventStatus;
}
