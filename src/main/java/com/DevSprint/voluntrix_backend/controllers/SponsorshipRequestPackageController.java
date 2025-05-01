package com.DevSprint.voluntrix_backend.controllers;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.DevSprint.voluntrix_backend.entities.SponsorshipRequestPackage;
import com.DevSprint.voluntrix_backend.services.SponsorshipRequestPackageService;


import io.swagger.v3.oas.annotations.parameters.RequestBody;


@RestController
@RequestMapping("/api/public/sponsorship-request-packages")
public class SponsorshipRequestPackageController {
    private final SponsorshipRequestPackageService service;

    public SponsorshipRequestPackageController(SponsorshipRequestPackageService service) {
        this.service = service;
    }

    @GetMapping
    public List<SponsorshipRequestPackage> getAll() {
        return service.getAllRequestPackages();
    }

    @GetMapping("/{id}")
    public SponsorshipRequestPackage getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public SponsorshipRequestPackage create(@RequestBody SponsorshipRequestPackage item) {
        return service.create(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
