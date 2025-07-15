package com.DevSprint.voluntrix_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.EventInvitationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventInvitationDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.EventInvitationEntity;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.exceptions.DuplicateInvitationException;
import com.DevSprint.voluntrix_backend.exceptions.EventInvitationNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.OrganizationNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.EventInvitationRepository;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.utils.EventDTOConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventInvitationService {

    private final EventInvitationRepository eventInvitationRepository;
    private final EventRepository eventRepository;
    private final OrganizationRepository organizationRepository;
    private final EventDTOConverter entityDTOConvert;

    public void addEventInvitation(EventInvitationCreateDTO eventInvitationCreateDTO) {
        if (eventInvitationCreateDTO.getEventId() == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }

        if (eventInvitationCreateDTO.getOrganizationId() == null) {
            throw new IllegalArgumentException("Organization ID cannot be null");
        }

        EventEntity event = eventRepository.findById(eventInvitationCreateDTO.getEventId())
                .orElseThrow(() -> new EventNotFoundException(
                        "Event not found: " + eventInvitationCreateDTO.getEventId()));

        OrganizationEntity organization = organizationRepository.findById(eventInvitationCreateDTO.getOrganizationId())
                .orElseThrow(() -> new OrganizationNotFoundException(
                        "Organization not found: " + eventInvitationCreateDTO.getOrganizationId()));

        boolean alreadyApplied = eventInvitationRepository.existsByEvent(event);
        if (alreadyApplied) {
            throw new DuplicateInvitationException(
                    "Only one invitation can be send. Event ID:"
                            + eventInvitationCreateDTO.getEventId());
        }

        boolean alreadyInvited = eventInvitationRepository.existsByEventAndOrganization(event, organization);
        if (alreadyInvited) {
            throw new DuplicateInvitationException(
                    "Invitation already exists for this organization and event. Organization ID:"
                            + eventInvitationCreateDTO.getOrganizationId() + " Event ID:"
                            + eventInvitationCreateDTO.getEventId());
        }

        EventInvitationEntity eventInvitationEntity = entityDTOConvert
                .toEventInvitationEntity(eventInvitationCreateDTO, event, organization);
        eventInvitationRepository.save(eventInvitationEntity);
    }

    public List<EventInvitationDTO> getAllEventInvitations() {
        return entityDTOConvert.toEventInvitationDTOList(eventInvitationRepository.findAll());
    }

    public EventInvitationDTO getEventInvitationById(Long id) {
        EventInvitationEntity eventInvitationEntity = eventInvitationRepository.findById(id)
                .orElseThrow(() -> new EventInvitationNotFoundException("Event invitation not found with ID: " + id));

        return entityDTOConvert.toEventInvitationDTO(eventInvitationEntity);
    }

    public void updateEventInvitation(EventInvitationCreateDTO eventInvitationCreateDTO, Long id) {
        EventInvitationEntity selectedInvitation = eventInvitationRepository.findById(id)
                .orElseThrow(() -> new EventInvitationNotFoundException("Event invitation not found with ID: " + id));

        EventEntity event = selectedInvitation.getEvent();
        OrganizationEntity organization = selectedInvitation.getOrganization();

        if (eventInvitationCreateDTO.getEventId() != null) {
            event = eventRepository.findById(eventInvitationCreateDTO.getEventId())
                    .orElseThrow(() -> new EventNotFoundException(
                            "Event not found: " + eventInvitationCreateDTO.getEventId()));
        }

        if (eventInvitationCreateDTO.getOrganizationId() != null) {
            organization = organizationRepository.findById(eventInvitationCreateDTO.getOrganizationId())
                    .orElseThrow(() -> new OrganizationNotFoundException(
                            "Organization not found: " + eventInvitationCreateDTO.getOrganizationId()));
        }

        if (eventInvitationCreateDTO.getEventId() != null || eventInvitationCreateDTO.getOrganizationId() != null) {
            Optional<EventInvitationEntity> existingInvitation = eventInvitationRepository
                    .findByEventAndOrganization(event, organization);

            if (existingInvitation.isPresent() && !existingInvitation.get().getId().equals(id)) {
                throw new DuplicateInvitationException(
                        "Invitation already exists for this organization and event. Organization ID:"
                                + eventInvitationCreateDTO.getOrganizationId() + " Event ID:"
                                + eventInvitationCreateDTO.getEventId());
            }

            selectedInvitation.setEvent(event);
            selectedInvitation.setOrganization(organization);
        }

        if (eventInvitationCreateDTO.getApplicationStatus() != null) {
            selectedInvitation.setApplicationStatus(eventInvitationCreateDTO.getApplicationStatus());
        }

        eventInvitationRepository.save(selectedInvitation);
    }

    public void deleteEventInvitation(Long id) {
        EventInvitationEntity invitation = eventInvitationRepository.findById(id)
                .orElseThrow(() -> new EventInvitationNotFoundException("Event invitation not found"));

        if (invitation.getEvent() != null) {
            invitation.getEvent().setInvitation(null);
        }

        eventInvitationRepository.deleteById(id);
    }
}