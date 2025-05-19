package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.entities.Sponsorship;
import com.DevSprint.voluntrix_backend.services.SponsorshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/sponsorships")
public class SponsorshipController {

    private final SponsorshipService service;

    public SponsorshipController(SponsorshipService service) {
        this.service = service;
    }

    @GetMapping
    public List<Sponsorship> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sponsorship> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sponsorship create(@RequestBody Sponsorship sponsorship) {
        return service.create(sponsorship);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sponsorship> update(@PathVariable Long id, @RequestBody Sponsorship sponsorship) {
        return service.update(id, sponsorship)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
