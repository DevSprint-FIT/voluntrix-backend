package com.DevSprint.voluntrix_backend.analytics.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.analytics.dto.MonthlyDonationData;
import com.DevSprint.voluntrix_backend.analytics.services.OrganizationPaymentAnalyticsService;

@RestController
@RequestMapping("/api/analytics/organizations")
public class OrganizationAnalyticsController {

    private final OrganizationPaymentAnalyticsService organizationPaymentAnalyticsService;

    @Autowired
    public OrganizationAnalyticsController(OrganizationPaymentAnalyticsService organizationPaymentAnalyticsService) {
        this.organizationPaymentAnalyticsService = organizationPaymentAnalyticsService;
    }

    @GetMapping("/{organizationId}/donations/this-month")
    public Double getThisMonthsDonations(@PathVariable Long organizationId) {
        return organizationPaymentAnalyticsService.getThisMonthsTotalDonations(organizationId);
    }

    @GetMapping("/{organizationId}/donations/monthly")
    public List<MonthlyDonationData> getMonthlyDonations(@PathVariable Long organizationId, @RequestParam int year) {
        return organizationPaymentAnalyticsService.getMonthlyDonations(organizationId, year);
    }
}

