package com.DevSprint.voluntrix_backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.MonthlyDonationData;
import com.DevSprint.voluntrix_backend.dtos.MonthlyDonationDto;
import com.DevSprint.voluntrix_backend.repositories.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VolunteerPaymentAnalyticsService {
    
    private final PaymentRepository paymentRepository;

    public List<MonthlyDonationDto> getMonthlyDonations(Long volunteerId, int year) {
        List<MonthlyDonationData> rawData = paymentRepository.findMonthlyDonationsByVolunteerAndYear(volunteerId, year);

        return rawData.stream()
            .map(data -> new MonthlyDonationDto(data.getMonth(), data.getTotal()))
            .collect(Collectors.toList());
    }

    public Double getAllTimeTotalDonation(Long volunteerId) {
        return paymentRepository.sumTotalDonationsByVolunteer(volunteerId);
    }


}
