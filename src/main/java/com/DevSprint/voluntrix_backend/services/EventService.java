package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.enums.EventStatus;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.utils.EventEntityDTOConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventEntityDTOConverter converter;

    public List<EventDTO> getActiveEventsByOrganization(Long organizationId) {
        List<EventEntity> activeEvents = eventRepository.findByOrganizationIdAndEventStatus(
                organizationId,
                EventStatus.ACTIVE
        );
        return converter.toEventDTOList(activeEvents);
    }

    public long getActiveEventCount(Long organizationId){
        return eventRepository.countByOrganizationIdAndEventStatus(organizationId, EventStatus.ACTIVE);
    }

    public long getPendingEventCount(Long organizationId) {
        return eventRepository.countByOrganizationIdAndEventStatus(organizationId, EventStatus.PENDING);
    }

    public long getCompletedEventCount(Long organizationId) {
        return eventRepository.countByOrganizationIdAndEventStatus(organizationId, EventStatus.COMPLETE);
    }


}
