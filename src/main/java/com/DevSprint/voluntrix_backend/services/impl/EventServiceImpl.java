package com.DevSprint.voluntrix_backend.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.enums.EventType;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.services.EventService;
import com.DevSprint.voluntrix_backend.utils.EntityDTOConvert;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EntityDTOConvert entityDTOConvert;

    @Override
    public void addEvent(EventDTO eventDTO) {
        var eventEntity = entityDTOConvert.toEventEntity(eventDTO);
        eventRepository.save(eventEntity);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found"));
        eventRepository.deleteById(eventId);
    }

    @Override
    public EventDTO getEventById(Long eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found"));
        return entityDTOConvert.toEventDTO(eventRepository.getReferenceById(eventId));
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return entityDTOConvert.toEventDTOList(eventRepository.findAll());
    }

    @Override
    public void updateEvent(Long eventId, EventDTO eventDTO) {
        var selectedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        selectedEvent.setEventTitle(eventDTO.getEventTitle());
        selectedEvent.setEventDescription(eventDTO.getEventDescription());
        selectedEvent.setEventLocation(eventDTO.getEventLocation());
        selectedEvent.setEventDate(eventDTO.getEventDate());
        selectedEvent.setEventTime(eventDTO.getEventTime());
        selectedEvent.setEventImageUrl(eventDTO.getEventImageUrl());
        selectedEvent.setEventType(eventDTO.getEventType());
        selectedEvent.setEventVisibility(eventDTO.getEventVisibility());
        selectedEvent.setEventStatus(eventDTO.getEventStatus());
        selectedEvent.setSponsorshipEnabled(eventDTO.getSponsorshipEnabled());
        selectedEvent.setDonationEnabled(eventDTO.getDonationEnabled());
    }

    @Override
    public List<EventDTO> getFilterEvent(String eventLocation, LocalDate eventDate, EventType eventType) {
        Specification<EventEntity> spec = Specification.where(null);

        if (eventType != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("eventType"), eventType));
        }

        if (eventLocation != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("eventLocation"), "%" + eventLocation + "%"));
        }

        if (eventDate != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("eventDate"), eventDate));
        }

        return entityDTOConvert.toEventDTOList(eventRepository.findAll(spec));
    }
}
