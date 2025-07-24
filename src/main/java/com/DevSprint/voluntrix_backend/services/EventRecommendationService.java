package com.DevSprint.voluntrix_backend.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
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

    public List<EventDTO> getRecommendedEvents(Long volunteerId) {
        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId));

        List<EventEntity> allEvents = eventRepository.findAll();

        List<EventEntity> filteredEvents = allEvents.stream()
                .filter(event -> event.getEventStatus() == EventStatus.ACTIVE)
                .filter(event -> !hasAppliedOrParticipated(volunteer, event))
                .filter(event -> isEventVisibleToVolunteer(volunteer, event))
                .filter(event -> !event.getEventStartDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        return entityDTOConvert.toEventDTOList(filteredEvents);
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
}
