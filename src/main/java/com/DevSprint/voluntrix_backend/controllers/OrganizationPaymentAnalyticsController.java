package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.MonthlyDonationData;
import com.DevSprint.voluntrix_backend.services.OrganizationPaymentAnalyticsService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.UserType;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/analytics/organizations")
public class OrganizationPaymentAnalyticsController {

    private final OrganizationPaymentAnalyticsService organizationPaymentAnalyticsService;

    @GetMapping("/{organizationId}/donations/this-month")
    @RequiresRole(UserType.ORGANIZATION)
    public Double getThisMonthsDonations(@PathVariable Long organizationId) {
        return organizationPaymentAnalyticsService.getThisMonthsTotalDonations(organizationId);
    }

    @GetMapping("/{organizationId}/donations/monthly")
    @RequiresRole(UserType.ORGANIZATION)
    public List<MonthlyDonationData> getMonthlyDonations(@PathVariable Long organizationId, @RequestParam int year) {
        return organizationPaymentAnalyticsService.getMonthlyDonations(organizationId, year);
    }
}

