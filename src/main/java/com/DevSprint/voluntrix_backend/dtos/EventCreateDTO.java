package com.DevSprint.voluntrix_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.enums.EventType;
import com.DevSprint.voluntrix_backend.enums.EventVisibility;
import com.fasterxml.jackson.annotation.JsonFormat;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventCreateDTO implements Serializable {
    private String eventTitle;
    private String eventDescription;
    private String eventLocation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventEndDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime eventTime;

    private String eventImageUrl;

    private EventType eventType; // ONLINE or ONSITE
    private EventVisibility eventVisibility; // PRIVATE or PUBLIC
    private EventStatus eventStatus; // DRAFT, PENDING, ACTIVE, COMPLETE, DENIED

    private Boolean sponsorshipEnabled;
    private Boolean donationEnabled;

    private Set<CategoryDTO> categories;

    private Long eventHostId;

    // private Long organizationId;
}

