package com.DevSprint.voluntrix_backend.services.impl;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.services.EventService;
import com.DevSprint.voluntrix_backend.utils.EntityDTOConvert;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService{

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
        entityDTOConvert.toEventDTO(eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found")));
        return entityDTOConvert.toEventDTO(eventRepository.getReferenceById(eventId));
    }
}
