package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.EventApplicationCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.EventApplicationDTO;
import com.DevSprint.voluntrix_backend.exceptions.EventApplicationNotFoundException;
import com.DevSprint.voluntrix_backend.services.EventApplicationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/event-applications")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EventapplicationController {

    private final EventApplicationService eventApplicationService;

    @GetMapping
    public ResponseEntity<List<EventApplicationDTO>> getAllEventApplications() {
        return new ResponseEntity<List<EventApplicationDTO>>(eventApplicationService.getAllEventApplications(),
                HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addEventApplication(@RequestBody EventApplicationCreateDTO eventApplicationCreateDTO) {
        if (eventApplicationCreateDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            eventApplicationService.addEventApplication(eventApplicationCreateDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{eventApplicationId}")
    public ResponseEntity<EventApplicationDTO> getEventApplicationById(@PathVariable Long eventApplicationId) {
        if (eventApplicationId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            var selectedApplication = eventApplicationService.getEventApplicationById(eventApplicationId);
            return new ResponseEntity<>(selectedApplication, HttpStatus.OK);
        } catch (EventApplicationNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/{eventApplicationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateEventApplication(@PathVariable Long eventApplicationId,
            @Valid @RequestBody EventApplicationCreateDTO eventApplicationCreateDTO) {
        if (eventApplicationId == null || eventApplicationCreateDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            eventApplicationService.updateEventApplication(eventApplicationCreateDTO, eventApplicationId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{eventApplicationId}")
    public ResponseEntity<Void> deleteEventApplication(@PathVariable Long eventApplicationId) {
        if (eventApplicationId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            eventApplicationService.deleteEventApplication(eventApplicationId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EventApplicationNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}