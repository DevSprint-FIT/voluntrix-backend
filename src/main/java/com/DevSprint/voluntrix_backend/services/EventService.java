package com.DevSprint.voluntrix_backend.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.EventCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.dtos.EventNameDTO;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.enums.EventVisibility;
import com.DevSprint.voluntrix_backend.exceptions.CategoryNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.BadRequestException;
import com.DevSprint.voluntrix_backend.repositories.CategoryRepository;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
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
    private final VolunteerRepository volunteerRepository;

    public void addEvent(EventCreateDTO eventCreateDTO) {
        VolunteerEntity eventHost = volunteerRepository.findById(eventCreateDTO.getEventHostId())
                .orElseThrow(() -> new VolunteerNotFoundException(
                        "Event Host not found: " + eventCreateDTO.getEventHostId()));

        if (!Boolean.TRUE.equals(eventHost.getIsEventHost())) {
            throw new BadRequestException("Volunteer is not an event host");
        }

        EventEntity eventEntity = entityDTOConvert.toEventEntity(eventCreateDTO, eventHost);
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
        selectedEvent.setEventStartDate(eventDTO.getEventStartDate());
        selectedEvent.setEventEndDate(eventDTO.getEventEndDate());
        selectedEvent.setEventTime(eventDTO.getEventTime());
        selectedEvent.setEventImageUrl(eventDTO.getEventImageUrl());
        selectedEvent.setVolunteerCount(eventDTO.getVolunteerCount());
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

    public List<EventDTO> getFilterEvent(String eventLocation, LocalDate startDate, LocalDate endDate,
            EventVisibility eventVisibility, List<Long> categoryIds) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        Specification<EventEntity> spec = Specification.where(null);

        if (eventVisibility != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("eventVisibility"),
                    eventVisibility));
        }

        if (eventLocation != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("eventLocation"),
                    "%" + eventLocation + "%"));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("eventStartDate"), startDate,
                            endDate));
        } else if (startDate != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .greaterThanOrEqualTo(root.get("eventStartDate"), startDate));
        } else if (endDate != null) {
            spec = spec
                    .and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("eventStartDate"),
                            endDate));
        }

        if (categoryIds != null && !categoryIds.isEmpty()) {

            for (Long id : categoryIds) {
                categoryRepository.findById(id)
                        .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + id));
            }

            spec = spec.and((root, query, criteriaBuilder) -> {
                if (query != null) {
                    query.distinct(true);
                }
                return root.join("categories").get("categoryId").in(categoryIds);
            });
        }

        return entityDTOConvert.toEventDTOList(eventRepository.findAll(spec));
    }

    public List<EventNameDTO> getAllEventNames() {
        return eventRepository.findAllEventIdAndTitle();
    }

    public List<EventDTO> searchEvents(String query) {
        return entityDTOConvert.toEventDTOList(eventRepository.findByEventTitleContainingIgnoreCase(query));
    }
}