package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.exceptions.ResourceNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.utils.EventEntityDTOConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class OrganizationEventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventEntityDTOConverter converter;

    // Get events filtered by status
    public List<EventDTO> getEventsByOrganizationAndStatus(Long organizationId, EventStatus status) {
        List<EventEntity> events = eventRepository.findByOrganizationIdAndEventStatus(organizationId, status);
        if (events.isEmpty()){
            throw new ResourceNotFoundException("No events found for organization ID: " + organizationId + " and status: " + status);
        }
        return converter.toEventDTOList(events);
    }

    // Get all events without filtering
    public List<EventDTO> getAllEventsByOrganization(Long organizationId) {
        List<EventEntity> events = eventRepository.findByOrganizationId(organizationId);
        return converter.toEventDTOList(events);
    }

    // Status count logic
    public Map<String, Long> getEventStatusCounts(Long organizationId) {
        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("active", eventRepository.countByOrganizationIdAndEventStatus(organizationId, EventStatus.ACTIVE));
        statusCounts.put("pending", eventRepository.countByOrganizationIdAndEventStatus(organizationId, EventStatus.PENDING));
        statusCounts.put("completed", eventRepository.countByOrganizationIdAndEventStatus(organizationId, EventStatus.COMPLETE));
        return statusCounts;
    }


}
