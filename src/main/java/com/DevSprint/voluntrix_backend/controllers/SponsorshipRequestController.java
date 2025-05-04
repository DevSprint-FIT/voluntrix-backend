package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.entities.SponsorshipRequestEntity;
import com.DevSprint.voluntrix_backend.services.SponsorshipRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/sponsorship-requests")
@RequiredArgsConstructor
public class SponsorshipRequestController {

    private final SponsorshipRequestService sponsorshipRequestService;

    @GetMapping
    public List<SponsorshipRequestEntity> getAllRequests() {
        return sponsorshipRequestService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SponsorshipRequestEntity> getRequestById(@PathVariable Long id) {
        return sponsorshipRequestService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SponsorshipRequestEntity createRequest(@RequestBody SponsorshipRequestEntity sponsorshipRequest) {
        return sponsorshipRequestService.create(sponsorshipRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SponsorshipRequestEntity> updateRequest(@PathVariable Long id, @RequestBody SponsorshipRequestEntity updatedRequest) {
        return sponsorshipRequestService.update(id, updatedRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        return sponsorshipRequestService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
