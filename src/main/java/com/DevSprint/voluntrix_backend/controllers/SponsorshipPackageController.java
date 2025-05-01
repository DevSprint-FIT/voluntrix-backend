package com.DevSprint.voluntrix_backend.controllers;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.entities.SponsorshipPackage;
import com.DevSprint.voluntrix_backend.services.SponsorshipPackageService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;



@RestController
@RequestMapping("/api/public/sponsorship-packages")
public class SponsorshipPackageController {
    private final SponsorshipPackageService service;

    public SponsorshipPackageController(SponsorshipPackageService service) {
        this.service = service;
    }

    @GetMapping
    public List<SponsorshipPackage> getAll() {
        return service.getAllPackages();
    }

    @GetMapping("/{id}")
    public SponsorshipPackage getById(@PathVariable Long id) {
        return service.getPackageById(id);
    }

    @PostMapping
    public SponsorshipPackage create(@RequestBody SponsorshipPackage pkg) {
        return service.createPackage(pkg);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePackage(id);
    }
}
