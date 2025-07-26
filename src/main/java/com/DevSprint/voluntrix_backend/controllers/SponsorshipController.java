package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipDTO;
import com.DevSprint.voluntrix_backend.services.SponsorshipService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/sponsorships")
@SecurityRequirement(name = "bearerAuth")
public class SponsorshipController {

    private final SponsorshipService sponsorshipService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> addSponsorship(@RequestBody SponsorshipCreateDTO sponsorshipCreateDTO) {
        if (sponsorshipCreateDTO == null || sponsorshipCreateDTO.getEventId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        sponsorshipService.addSponsorship(sponsorshipCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SponsorshipDTO>> getAllSponsorships() {
        return new ResponseEntity<List<SponsorshipDTO>>(sponsorshipService.getAllSponsorships(), HttpStatus.OK);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<SponsorshipDTO>> getAllSponsorshipsByEventId(@PathVariable Long eventId) {
        List<SponsorshipDTO> sponsorships = sponsorshipService.getAllSponsorshipsByEventId(eventId);

        return new ResponseEntity<>(sponsorships, HttpStatus.OK);
    }
}
