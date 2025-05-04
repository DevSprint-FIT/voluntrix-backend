package com.DevSprint.voluntrix_backend.services;

import java.util.List;

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
            throw new DuplicateApplicationException("Application already exists for this volunteer and event."
                    + eventApplicationCreateDTO.getVolunteerId() + eventApplicationCreateDTO.getEventId());
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
                .orElseThrow(() -> new EventApplicationNotFoundException("Event application not found"));

        return entityDTOConvert.toEventApplicationDTO(eventApplicationEntity);
    }

    public void updateEventApplication(EventApplicationCreateDTO eventApplicationCreateDTO, Long id) {
        EventApplicationEntity selectedApplication = eventApplicationRepository.findById(id)
                .orElseThrow(() -> new EventApplicationNotFoundException("Event application not found"));

        if (eventApplicationCreateDTO.getEventId() != null) {
            EventEntity event = eventRepository.findById(eventApplicationCreateDTO.getEventId())
                    .orElseThrow(() -> new EventNotFoundException(
                            "Event not found: " + eventApplicationCreateDTO.getEventId()));

            selectedApplication.setEvent(event);

        }

        if (eventApplicationCreateDTO.getEventId() != null) {
            VolunteerEntity volunteer = volunteerRepository.findById(eventApplicationCreateDTO.getVolunteerId())
                    .orElseThrow(() -> new VolunteerNotFoundException(
                            "Volunteer not found: " + eventApplicationCreateDTO.getVolunteerId()));

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