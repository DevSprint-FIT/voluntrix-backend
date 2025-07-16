package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.MonthlyDonationDTO;
import com.DevSprint.voluntrix_backend.services.VolunteerPaymentAnalyticsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/analytics/volunteer")
public class VolunteerPaymentAnalyticsController {
    
    private final VolunteerPaymentAnalyticsService volunteerPaymentAnalyticsService;

    @GetMapping("/{volunteerId}/donations/monthly")
    public ResponseEntity<List<MonthlyDonationDTO>> getMonthlyDonationsByYear(@PathVariable Long volunteerId, @RequestParam int year) {
        List<MonthlyDonationDTO> monthlyDonation = volunteerPaymentAnalyticsService.getMonthlyDonations(volunteerId, year);
        return ResponseEntity.ok(monthlyDonation);
    }
    
    @GetMapping("/{volunteerId}/donations/total")
    public Double getTotalDonationsByVolunteer(@PathVariable Long volunteerId) {
        Double totalDonation = volunteerPaymentAnalyticsService.getAllTimeTotalDonation(volunteerId);
        return totalDonation != null ? totalDonation : 0;
    } 
}
