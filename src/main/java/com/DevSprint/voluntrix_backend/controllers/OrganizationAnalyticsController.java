package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.services.OrganizationPaymentAnalyticsService;

@RestController
@RequestMapping("/api/analytics/organizations")
public class OrganizationAnalyticsController {

    private final OrganizationPaymentAnalyticsService organizationAnalyticsService;

    @Autowired
    public OrganizationAnalyticsController(OrganizationPaymentAnalyticsService organizationPaymentAnalyticsService) {
        this.organizationAnalyticsService = organizationPaymentAnalyticsService;
    }

    @GetMapping("/{organizationId}/donations/this-month")
    public Double getThisMonthsDonations(@PathVariable Long organizationId) {
        return organizationAnalyticsService.getThisMonthsTotalDonations(organizationId);
    }
}

