package com.DevSprint.voluntrix_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.repositories.PaymentRepository;

@Service
public class OrganizationPaymentAnalyticsService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public OrganizationPaymentAnalyticsService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Double getThisMonthsTotalDonations(Long organizationId) {
        return paymentRepository.sumDonationsForOrganizationThisMonth(organizationId);
    }
}

