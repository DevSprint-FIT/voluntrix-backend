package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.exceptions.*;
import com.DevSprint.voluntrix_backend.repositories.*;

import com.DevSprint.voluntrix_backend.utils.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrganizationEventService {

    private final EventRepository eventRepository;
    private final EventDTOConverter eventDTOConverter;
    private final OrganizationRepository organizationRepository;

    // Get events filtered by status
    public List<EventDTO> getEventsByOrganizationAndStatus(Long organizationId, EventStatus status) {
        notExistOrganizationId(organizationId);
        List<EventEntity> events = eventRepository.findByOrganizationIdAndEventStatus(organizationId, status);
        if (events.isEmpty()){
            throw new ResourceNotFoundException("No events found for organization ID: " + organizationId + " and status: " + status);
        }
        return eventDTOConverter.toEventDTOList(events);
    }

    // Get all events without filtering
    public List<EventDTO> getAllEventsByOrganization(Long organizationId) {
        notExistOrganizationId(organizationId);
        List<EventEntity> events = eventRepository.findByOrganizationId(organizationId);
        return eventDTOConverter.toEventDTOList(events);
    }

    // Status count logic
    public Map<String, Long> getEventStatusCounts(Long organizationId) {
        notExistOrganizationId(organizationId);

        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("active", eventRepository.countByOrganizationIdAndEventStatus(organizationId, EventStatus.ACTIVE));
        statusCounts.put("pending", eventRepository.countByOrganizationIdAndEventStatus(organizationId, EventStatus.PENDING));
        statusCounts.put("completed", eventRepository.countByOrganizationIdAndEventStatus(organizationId, EventStatus.COMPLETE));
        return statusCounts;
    }

    private void notExistOrganizationId(Long organizationId) {
        if (!organizationRepository.existsById(organizationId)) {
            throw new OrganizationNotFoundException("Organization not found with id: " + organizationId);
        }
    }

}
