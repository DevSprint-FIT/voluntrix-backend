package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
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
    public List<SponsorEntity> getAllSponsors() {
        return sponsorService.getAllSponsors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SponsorEntity> getSponsorById(@PathVariable Long id) {
        return sponsorService.getSponsorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SponsorEntity createSponsor(@RequestBody SponsorEntity sponsor) {
        return sponsorService.createSponsor(sponsor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SponsorEntity> updateSponsor(@PathVariable Long id, @RequestBody SponsorEntity sponsor) {
        SponsorEntity updated = sponsorService.updateSponsor(id, sponsor);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSponsor(@PathVariable Long id) {
        sponsorService.deleteSponsor(id);
        return ResponseEntity.noContent().build();
    }
}
