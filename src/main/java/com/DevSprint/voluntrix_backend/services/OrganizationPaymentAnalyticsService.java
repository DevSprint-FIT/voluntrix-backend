package com.DevSprint.voluntrix_backend.services;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.DevSprint.voluntrix_backend.dtos.MonthlyDonationData;
import com.DevSprint.voluntrix_backend.repositories.PaymentRepository;

@Service
@RequiredArgsConstructor
public class OrganizationPaymentAnalyticsService {

    private final PaymentRepository paymentRepository;

    public Double getThisMonthsTotalDonations(Long organizationId) {
        return paymentRepository.sumDonationsForOrganizationThisMonth(organizationId);
    }

    public List<MonthlyDonationData> getMonthlyDonations(Long organizationId, int year) {
        return paymentRepository.getMonthlyDonationsForOrganizationAndYear(organizationId, year);
    }
}

