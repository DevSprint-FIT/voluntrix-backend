package com.DevSprint.voluntrix_backend.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.EventDTO;
import com.DevSprint.voluntrix_backend.dtos.EventNameDTO;
import com.DevSprint.voluntrix_backend.enums.EventVisibility;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
import com.DevSprint.voluntrix_backend.services.EventService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/v1/events")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addEvent(@RequestBody EventDTO eventDTO) {

        if (eventDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (eventDTO.getEventStartDate().isAfter(eventDTO.getEventEndDate())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        eventService.addEvent(eventDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {

        if (eventId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            eventService.deleteEvent(eventId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EventNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long eventId) {

        if (eventId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            var selectedEvent = eventService.getEventById(eventId);
            return new ResponseEntity<EventDTO>(selectedEvent, HttpStatus.OK);
        } catch (EventNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return new ResponseEntity<List<EventDTO>>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @PatchMapping(value = "/{eventId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateEvent(@PathVariable Long eventId, @Valid @RequestBody EventDTO eventDTO) {

        if (eventId == null || eventDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            eventService.updateEvent(eventId, eventDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EventNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventDTO>> getFilteredEvent(
            @RequestParam(value = "eventLocation", required = false) String eventLocation,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "eventVisibility", required = false) EventVisibility eventVisibility,
            @RequestParam(value = "categoryIds", required = false) List<Long> categoryIds) {

        if (eventLocation == null && startDate == null && endDate == null && eventVisibility == null
                && categoryIds == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        try {
            List<EventDTO> filteredEventList = eventService.getFilterEvent(eventLocation, startDate, endDate,
                    eventVisibility, categoryIds);
            return new ResponseEntity<List<EventDTO>>(filteredEventList, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/names")
    public ResponseEntity<List<EventNameDTO>> getAllEventNames() {
        return new ResponseEntity<List<EventNameDTO>>(eventService.getAllEventNames(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> searchEvents(@RequestParam String query) {
        return new ResponseEntity<List<EventDTO>>(eventService.searchEvents(query), HttpStatus.OK);
    }
}
