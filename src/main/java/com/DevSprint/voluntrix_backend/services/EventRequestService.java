package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.EventRequestDTO;
import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventRequestService {

    @Autowired
    private EventRepository eventRepository;

    // Fetch pending event names for a given organization
    public List<EventRequestDTO> getPendingEventsByOrganization(Long organizationId) {
        return eventRepository.findByOrganizationIdAndEventStatus(organizationId, EventStatus.PENDING)
                .stream()
                .map(event -> new EventRequestDTO(event.getEventTitle()))
                .collect(Collectors.toList());
    }
}
