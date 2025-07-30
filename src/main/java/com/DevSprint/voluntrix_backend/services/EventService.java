package com.DevSprint.voluntrix_backend.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.EventAndOrgDTO;
import com.DevSprint.voluntrix_backend.dtos.EventCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.dtos.EventNameDTO;
import com.DevSprint.voluntrix_backend.dtos.EventStatusUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.enums.EventVisibility;
import com.DevSprint.voluntrix_backend.exceptions.CategoryNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.OrganizationNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.BadRequestException;
import com.DevSprint.voluntrix_backend.repositories.CategoryRepository;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.utils.EventDTOConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventDTOConverter entityDTOConvert;
    private final CategoryRepository categoryRepository;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final RewardService rewardService;

    public EventEntity addEvent(EventCreateDTO eventCreateDTO, Long eventHostId) {
        VolunteerEntity eventHost = volunteerRepository.findById(eventHostId)
                .orElseThrow(() -> new VolunteerNotFoundException(
                        "Event Host not found: " + eventHostId));

        if (eventCreateDTO.getOrganizationId() != null) {
            organizationRepository.findById(eventCreateDTO.getOrganizationId())
                    .orElseThrow(() -> new OrganizationNotFoundException(
                            "Organization not found: " + eventCreateDTO.getOrganizationId()));
        }

        if (eventCreateDTO.getEventStartDate().isAfter(eventCreateDTO.getEventEndDate())) {
            throw new BadRequestException("Event start date cannot be after the event end date.");
        }

        if (!Boolean.TRUE.equals(eventHost.getIsEventHost())) {
            throw new BadRequestException("Volunteer is not an event host");
        }

        EventEntity eventEntity = entityDTOConvert.toEventEntity(eventCreateDTO, eventHost);
        EventEntity savedEvent = eventRepository.save(eventEntity);

        // Give 10 points to the event host
        rewardService.processEventCreation(savedEvent);

        return savedEvent;
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

    public EventAndOrgDTO getEventAndOrgById(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        return entityDTOConvert.toEventAndOrgDTO(eventEntity);
    }

    public EventEntity getEventEntityById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
    }

    public List<EventDTO> getAllEvents() {
        return entityDTOConvert.toEventDTOList(eventRepository.findAll());
    }

    public void updateEvent(Long eventId, EventDTO eventDTO, Long eventHostId) {
        var selectedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (eventDTO.getEventTitle() != null) {
            selectedEvent.setEventTitle(eventDTO.getEventTitle());
        }
        if (eventDTO.getEventDescription() != null) {
            selectedEvent.setEventDescription(eventDTO.getEventDescription());
        }
        if (eventDTO.getEventLocation() != null) {
            selectedEvent.setEventLocation(eventDTO.getEventLocation());
        }
        if (eventDTO.getEventStartDate() != null) {
            if (eventDTO.getEventStartDate().isAfter(selectedEvent.getEventEndDate())) {
                throw new BadRequestException("Event start date cannot be after the event end date.");
            }

            selectedEvent.setEventStartDate(eventDTO.getEventStartDate());
        }
        if (eventDTO.getEventEndDate() != null) {
            if (selectedEvent.getEventStartDate().isAfter(eventDTO.getEventEndDate())) {
                throw new BadRequestException("Event start date cannot be after the event end date.");
            }

            selectedEvent.setEventEndDate(eventDTO.getEventEndDate());
        }
        if (eventDTO.getEventTime() != null) {
            selectedEvent.setEventTime(eventDTO.getEventTime());
        }
        if (eventDTO.getEventImageUrl() != null) {
            selectedEvent.setEventImageUrl(eventDTO.getEventImageUrl());
        }
        if (eventDTO.getVolunteerCount() != null) {
            selectedEvent.setVolunteerCount(eventDTO.getVolunteerCount());
        }
        if (eventDTO.getEventType() != null) {
            selectedEvent.setEventType(eventDTO.getEventType());
        }
        if (eventDTO.getEventVisibility() != null) {
            selectedEvent.setEventVisibility(eventDTO.getEventVisibility());
        }
        if (eventDTO.getEventStatus() != null) {
            selectedEvent.setEventStatus(eventDTO.getEventStatus());
        }
        if (eventDTO.getSponsorshipEnabled() != null) {
            selectedEvent.setSponsorshipEnabled(eventDTO.getSponsorshipEnabled());
        }
        if (eventDTO.getDonationEnabled() != null) {
            selectedEvent.setDonationEnabled(eventDTO.getDonationEnabled());
        }
        if (eventDTO.getSponsorshipProposalUrl() != null) {
            selectedEvent.setSponsorshipProposalUrl(eventDTO.getSponsorshipProposalUrl());
        }
        if (eventDTO.getDonationGoal() != null) {
            selectedEvent.setDonationGoal(eventDTO.getDonationGoal());
        }
        if (eventDTO.getCategories() != null) {
            Set<CategoryEntity> categoryEntities = eventDTO.getCategories().stream()
                    .map(dto -> categoryRepository.findById(dto.getCategoryId())
                            .orElseThrow(
                                    () -> new CategoryNotFoundException("Category not found: " + dto.getCategoryId())))
                    .collect(Collectors.toSet());

            selectedEvent.setCategories(categoryEntities);
        }
        if (eventHostId != null) {
            VolunteerEntity eventHost = volunteerRepository.findById(eventHostId)
                    .orElseThrow(() -> new VolunteerNotFoundException(
                            "Event Host not found: " + eventHostId));

            if (!Boolean.TRUE.equals(eventHost.getIsEventHost())) {
                throw new BadRequestException("Volunteer is not an event host");
            }

            selectedEvent.setEventHost(eventHost);
        }
        if (eventDTO.getOrganizationId() != null) {
            OrganizationEntity organization = organizationRepository.findById(eventDTO.getOrganizationId())
                    .orElseThrow(() -> new OrganizationNotFoundException("Organization not found: "
                            + eventDTO.getOrganizationId()));
            selectedEvent.setOrganization(organization);
        }
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

    public List<EventAndOrgDTO> getFilterEventWithOrg(String eventLocation, LocalDate startDate, LocalDate endDate,
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

        return entityDTOConvert.toEventAndOrgDTOList(eventRepository.findAll(spec));
    }

    public List<EventNameDTO> getAllEventNames() {
        return eventRepository.findAllEventIdAndTitle();
    }

    public List<EventDTO> searchEvents(String query) {
        return entityDTOConvert.toEventDTOList(eventRepository.findByEventTitleContainingIgnoreCase(query));
    }

    public List<EventAndOrgDTO> searchEventsWithOrg(String query) {
        List<EventEntity> events = eventRepository.findByEventTitleContainingIgnoreCase(query);
        return entityDTOConvert.toEventAndOrgDTOList(events);
    }

    public List<EventDTO> getEventsByHostId(Long hostId) {

        VolunteerEntity eventHost = volunteerRepository.findById(hostId)
                .orElseThrow(() -> new VolunteerNotFoundException(
                        "Event Host not found: " + hostId));

        if (!Boolean.TRUE.equals(eventHost.getIsEventHost())) {
            throw new BadRequestException("Volunteer is not an event host");
        }

        List<EventEntity> events = eventRepository.findByEventHost(eventHost);

        return entityDTOConvert.toEventDTOList(events);
    }

    public void updateEventStatus(Long eventId, EventStatusUpdateDTO statusUpdateDTO) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        if (statusUpdateDTO.getEventStatus() != null) {
            event.setEventStatus(statusUpdateDTO.getEventStatus());
        }

        eventRepository.save(event);
    }
}