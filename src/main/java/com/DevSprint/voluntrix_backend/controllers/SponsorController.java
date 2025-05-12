package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.SponsorRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorResponseDTO;
import com.DevSprint.voluntrix_backend.services.SponsorService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/sponsors")
@CrossOrigin
public class SponsorController {

    @Autowired
    private SponsorService sponsorService;

    @GetMapping
    public List<SponsorResponseDTO> getAllSponsors() {
        return sponsorService.getAllSponsors();
    }

@GetMapping("/{id}")
public ResponseEntity<SponsorResponseDTO> getSponsorById(@PathVariable Long id) {
    SponsorResponseDTO sponsor = sponsorService.getSponsorById(id);
    return ResponseEntity.ok(sponsor);
}


    @PostMapping
    public SponsorResponseDTO createSponsor(@Valid @RequestBody SponsorRequestDTO sponsorDTO) {
        return sponsorService.createSponsor(sponsorDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SponsorResponseDTO> updateSponsor(@PathVariable Long id, @Valid @RequestBody SponsorRequestDTO sponsorDTO) {
        SponsorResponseDTO updated = sponsorService.updateSponsor(id, sponsorDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        sponsorService.deleteSponsor(id);
        return ResponseEntity.noContent().build();
    }
}
