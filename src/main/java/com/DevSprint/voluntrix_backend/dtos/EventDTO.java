package com.DevSprint.voluntrix_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.enums.EventType;
import com.DevSprint.voluntrix_backend.enums.EventVisibility;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventDTO implements Serializable{
    private Long id;

    private String title;
    private String description;
    private String location;

    private LocalDate date;
    private LocalTime time;

    private String imageUrl;

    private EventType type; // ONLINE or ONSITE
    private EventVisibility visibility; // PRIVATE or PUBLIC
    private EventStatus status; // DRAFT, PENDING, ACTIVE, COMPLETE, DENIED

    private Boolean sponsorshipEnabled;
    private Boolean donationEnabled;
}
