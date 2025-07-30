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
public class EventDTO implements Serializable {
    private Long eventId;

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
    private Integer volunteerCount;

    private EventType eventType; // ONLINE or ONSITE
    private EventVisibility eventVisibility; // PRIVATE or PUBLIC
    private EventStatus eventStatus; // DRAFT, PENDING, ACTIVE, COMPLETE, DENIED

    private Boolean sponsorshipEnabled;
    private Boolean donationEnabled;

    private String sponsorshipProposalUrl;
    private Integer donationGoal;
    private Integer donations;
    private Long eventHostId;

    private Set<CategoryDTO> categories;

    private Long organizationId;

    private Integer eventHostRewardPoints;

}
