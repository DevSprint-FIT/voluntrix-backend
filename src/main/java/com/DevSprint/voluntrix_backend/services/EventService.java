package com.DevSprint.voluntrix_backend.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.dtos.EventNameDTO;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.enums.EventType;
import com.DevSprint.voluntrix_backend.exceptions.CategoryNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.CategoryRepository;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.utils.EntityDTOConvert;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EntityDTOConvert entityDTOConvert;
    private final CategoryRepository categoryRepository;

    public void addEvent(EventDTO eventDTO) {
        EventEntity eventEntity = entityDTOConvert.toEventEntity(eventDTO);
        eventRepository.save(eventEntity);
    }

    public void deleteEvent(Long eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found"));
        eventRepository.deleteById(eventId);
    }

    public EventDTO getEventById(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        return entityDTOConvert.toEventDTO(eventEntity);
    }

    public List<EventDTO> getAllEvents() {
        return entityDTOConvert.toEventDTOList(eventRepository.findAll());
    }

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

        Set<CategoryEntity> categoryEntities = eventDTO.getCategories().stream()
                .map(dto -> categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + dto.getCategoryId())))
                .collect(Collectors.toSet());

        selectedEvent.setCategories(categoryEntities);

        eventRepository.save(selectedEvent);
    }

    public List<EventDTO> getFilterEvent(String eventLocation, LocalDate eventDate, EventType eventType) {
        Specification<EventEntity> spec = Specification.where(null);

        if (eventType != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("eventType"), eventType));
        }

        if (eventLocation != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("eventLocation"),
                    "%" + eventLocation + "%"));
        }

        if (eventDate != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("eventDate"), eventDate));
        }

        return entityDTOConvert.toEventDTOList(eventRepository.findAll(spec));
    }

    public List<EventNameDTO> getAllEventNames() {
        return eventRepository.findAllEventIdAndTitle();
    }
}