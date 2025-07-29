package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.MonthlyDonationDTO;
import com.DevSprint.voluntrix_backend.services.VolunteerPaymentAnalyticsService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;
import com.DevSprint.voluntrix_backend.enums.UserType;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/analytics/volunteer")
@SecurityRequirement(name = "bearerAuth")
public class VolunteerPaymentAnalyticsController {
    
    private final VolunteerPaymentAnalyticsService volunteerPaymentAnalyticsService;
    private final CurrentUserService currentUserService;

    @GetMapping("/donations/monthly")
    @RequiresRole({UserType.VOLUNTEER})
    public ResponseEntity<List<MonthlyDonationDTO>> getMonthlyDonationsByYear(@RequestParam int year) {
        Long volunteerId = currentUserService.getCurrentEntityId();
        List<MonthlyDonationDTO> monthlyDonation = volunteerPaymentAnalyticsService.getMonthlyDonations(volunteerId, year);
        return ResponseEntity.ok(monthlyDonation);
    }

    @GetMapping("/donations/total")
    @RequiresRole({UserType.VOLUNTEER})
    public Double getTotalDonationsByVolunteer() {
        Long volunteerId = currentUserService.getCurrentEntityId();
        Double totalDonation = volunteerPaymentAnalyticsService.getAllTimeTotalDonation(volunteerId);
        return totalDonation != null ? totalDonation : 0;
    } 
}
