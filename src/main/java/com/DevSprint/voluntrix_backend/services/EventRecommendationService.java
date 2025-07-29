package com.DevSprint.voluntrix_backend.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.EventAndOrgDTO;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.enums.EventVisibility;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.EventApplicationRepository;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerEventParticipationRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.utils.EventDTOConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventRecommendationService {

    private final EventRepository eventRepository;
    private final EventApplicationRepository applicationRepository;
    private final VolunteerEventParticipationRepository participationRepository;
    private final VolunteerRepository volunteerRepository;
    private final EventDTOConverter entityDTOConvert;

    public List<EventAndOrgDTO> getRecommendedEvents(Long volunteerId) {
        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId));

        Set<CategoryEntity> volunteerCategories = volunteer.getFollowedCategories();

        // Fetch all ACTIVE events ordered by latest
        List<EventEntity> allActiveEvents = eventRepository
                .findByEventStatusOrderByEventStartDateDesc(EventStatus.ACTIVE);

        List<EventEntity> filteredEvents = allActiveEvents.stream()
                // Exclude already applied or participated
                .filter(event -> !hasAppliedOrParticipated(volunteer, event))
                // Check visibility
                .filter(event -> isEventVisibleToVolunteer(volunteer, event))
                // Exclude past events
                .filter(event -> !event.getEventStartDate().isBefore(LocalDate.now()))
                // Optional category filter
                .filter(event -> volunteerCategories == null || volunteerCategories.isEmpty()
                        || hasMatchingCategory(event.getCategories(), volunteerCategories))
                // Limit to 8
                .limit(8)
                .collect(Collectors.toList());

        return entityDTOConvert.toEventAndOrgDTOList(filteredEvents);
    }

    private boolean hasAppliedOrParticipated(VolunteerEntity volunteer, EventEntity event) {
        return participationRepository.existsByVolunteerAndEvent(volunteer, event)
                || applicationRepository.existsByVolunteerAndEvent(volunteer, event);
    }

    private boolean isEventVisibleToVolunteer(VolunteerEntity volunteer, EventEntity event) {
        if (event.getEventVisibility() == EventVisibility.PUBLIC) {
            return true;
        } else if (event.getEventVisibility() == EventVisibility.PRIVATE) {
            OrganizationEntity org = event.getOrganization();
            return org != null && org.getInstitute() != null
                    && org.getInstitute().equalsIgnoreCase(volunteer.getInstitute());
        }
        return false;
    }

    private boolean hasMatchingCategory(Set<CategoryEntity> eventCategories, Set<CategoryEntity> volunteerCategories) {
        if (eventCategories == null || volunteerCategories == null)
            return false;
        return eventCategories.stream().anyMatch(volunteerCategories::contains);
    }

    public List<EventAndOrgDTO> getLatestThreeEvents() {
        List<EventEntity> latestEvents = eventRepository
                .findTop3ByEventStatusOrderByEventStartDateDesc(EventStatus.ACTIVE);

        return entityDTOConvert.toEventAndOrgDTOList(latestEvents);
    }

}