package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorResponseDTO;
import com.DevSprint.voluntrix_backend.services.SponsorService;
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
        return sponsorService.getSponsorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SponsorResponseDTO createSponsor(@RequestBody SponsorDTO sponsorDTO) {
        return sponsorService.createSponsor(sponsorDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SponsorResponseDTO> updateSponsor(@PathVariable Long id, @RequestBody SponsorDTO sponsorDTO) {
        SponsorResponseDTO updated = sponsorService.updateSponsor(id, sponsorDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        sponsorService.deleteSponsor(id);
        return ResponseEntity.noContent().build();
    }
}
