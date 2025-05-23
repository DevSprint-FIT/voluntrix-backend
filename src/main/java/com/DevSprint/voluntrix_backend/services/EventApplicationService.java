package com.DevSprint.voluntrix_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.EventApplicationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventApplicationDTO;
import com.DevSprint.voluntrix_backend.entities.EventApplicationEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.exceptions.DuplicateApplicationException;
import com.DevSprint.voluntrix_backend.exceptions.EventApplicationNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.EventApplicationRepository;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.utils.EventDTOConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventApplicationService {

    private final EventApplicationRepository eventApplicationRepository;
    private final EventRepository eventRepository;
    private final VolunteerRepository volunteerRepository;
    private final EventDTOConverter entityDTOConvert;

    public void addEventApplication(EventApplicationCreateDTO eventApplicationCreateDTO) {
        if (eventApplicationCreateDTO.getEventId() == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }

        if (eventApplicationCreateDTO.getVolunteerId() == null) {
            throw new IllegalArgumentException("Volunteer ID cannot be null");
        }

        EventEntity event = eventRepository.findById(eventApplicationCreateDTO.getEventId())
                .orElseThrow(() -> new EventNotFoundException(
                        "Event not found: " + eventApplicationCreateDTO.getEventId()));

        VolunteerEntity volunteer = volunteerRepository.findById(eventApplicationCreateDTO.getVolunteerId())
                .orElseThrow(() -> new VolunteerNotFoundException(
                        "Volunteer not found: " + eventApplicationCreateDTO.getVolunteerId()));

        boolean alreadyApplied = eventApplicationRepository.existsByEventAndVolunteer(event, volunteer);
        if (alreadyApplied) {
            throw new DuplicateApplicationException(
                    "Application already exists for this volunteer and event. Volunteer ID:"
                            + eventApplicationCreateDTO.getVolunteerId() + " Event ID:"
                            + eventApplicationCreateDTO.getEventId());
        }

        EventApplicationEntity eventApplicationEntity = entityDTOConvert
                .toEventApplicationEntity(eventApplicationCreateDTO, event, volunteer);
        eventApplicationRepository.save(eventApplicationEntity);
    }

    public List<EventApplicationDTO> getAllEventApplications() {
        return entityDTOConvert.toEventApplicationDTOList(eventApplicationRepository.findAll());
    }

    public EventApplicationDTO getEventApplicationById(Long id) {
        EventApplicationEntity eventApplicationEntity = eventApplicationRepository.findById(id)
                .orElseThrow(() -> new EventApplicationNotFoundException("Event application not found with ID: " + id));

        return entityDTOConvert.toEventApplicationDTO(eventApplicationEntity);
    }

    public void updateEventApplication(EventApplicationCreateDTO eventApplicationCreateDTO, Long id) {
        EventApplicationEntity selectedApplication = eventApplicationRepository.findById(id)
                .orElseThrow(() -> new EventApplicationNotFoundException("Event application not found with ID: " + id));

        EventEntity event = selectedApplication.getEvent();
        VolunteerEntity volunteer = selectedApplication.getVolunteer();

        if (eventApplicationCreateDTO.getEventId() != null) {
            event = eventRepository.findById(eventApplicationCreateDTO.getEventId())
                    .orElseThrow(() -> new EventNotFoundException(
                            "Event not found: " + eventApplicationCreateDTO.getEventId()));
        }

        if (eventApplicationCreateDTO.getVolunteerId() != null) {
            volunteer = volunteerRepository.findById(eventApplicationCreateDTO.getVolunteerId())
                    .orElseThrow(() -> new VolunteerNotFoundException(
                            "Volunteer not found: " + eventApplicationCreateDTO.getVolunteerId()));
        }

        if (eventApplicationCreateDTO.getEventId() != null || eventApplicationCreateDTO.getVolunteerId() != null) {
            Optional<EventApplicationEntity> existingApplication = eventApplicationRepository
                    .findByEventAndVolunteer(event, volunteer);

            if (existingApplication != null && !existingApplication.get().getId().equals(id)) {
                throw new DuplicateApplicationException(
                        "Application already exists for this volunteer and event. Volunteer ID:"
                                + selectedApplication.getVolunteer().getVolunteerId() + " Event ID:"
                                + selectedApplication.getEvent().getEventId());
            }

            selectedApplication.setEvent(event);
            selectedApplication.setVolunteer(volunteer);
        }

        if (eventApplicationCreateDTO.getDescription() != null) {
            selectedApplication.setDescription(eventApplicationCreateDTO.getDescription());
        }

        if (eventApplicationCreateDTO.getContributionArea() != null) {
            selectedApplication.setContributionArea(eventApplicationCreateDTO.getContributionArea());
        }

        if (eventApplicationCreateDTO.getApplicationStatus() != null) {
            selectedApplication.setApplicationStatus(eventApplicationCreateDTO.getApplicationStatus());
        }

        eventApplicationRepository.save(selectedApplication);
    }

    public void deleteEventApplication(Long id) {
        eventApplicationRepository.findById(id)
                .orElseThrow(() -> new EventApplicationNotFoundException("Event application not found"));
        eventApplicationRepository.deleteById(id);
    }
}