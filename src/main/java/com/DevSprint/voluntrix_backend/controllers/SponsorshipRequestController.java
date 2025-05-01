package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.DevSprint.voluntrix_backend.dtos.RequestDto;
import com.DevSprint.voluntrix_backend.entities.SponsorshipRequest;
import com.DevSprint.voluntrix_backend.services.SponsorshipRequestService;

@RestController
@RequestMapping("/api/public/sponsorship-requests")
public class SponsorshipRequestController {

    private final SponsorshipRequestService service;

    public SponsorshipRequestController(SponsorshipRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<SponsorshipRequest> getAll() {
        return service.getAllRequests();
    }

    @GetMapping("/{id}")
    public SponsorshipRequest getById(@PathVariable Long id) {
        return service.getRequestById(id);
    }

    @PostMapping
    public SponsorshipRequest create(@RequestBody RequestDto request) {
        return service.createRequest(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteRequest(id);
    }
}
