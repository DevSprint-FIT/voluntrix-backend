package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.EventRequestDTO;
import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.DevSprint.voluntrix_backend.utils.EventEntityDTOConverter;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventRequestService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventEntityDTOConverter eventEntityDTOConverter;  // Inject Utility

    public List<EventRequestDTO> getPendingEventsByOrganization(Long organizationId) {
        return eventEntityDTOConverter.toEventRequestDTOList(
            eventRepository.findByOrganizationIdAndEventStatus(organizationId, EventStatus.PENDING)
        );
    }
}

