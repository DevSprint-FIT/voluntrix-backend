package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.MonthlyDonationData;
import com.DevSprint.voluntrix_backend.services.OrganizationPaymentAnalyticsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/analytics/organizations")
public class OrganizationPaymentAnalyticsController {

    private final OrganizationPaymentAnalyticsService organizationPaymentAnalyticsService;

    @GetMapping("/{organizationId}/donations/this-month")
    public Double getThisMonthsDonations(@PathVariable Long organizationId) {
        return organizationPaymentAnalyticsService.getThisMonthsTotalDonations(organizationId);
    }

    @GetMapping("/{organizationId}/donations/monthly")
    public List<MonthlyDonationData> getMonthlyDonations(@PathVariable Long organizationId, @RequestParam int year) {
        return organizationPaymentAnalyticsService.getMonthlyDonations(organizationId, year);
    }
}

