package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.Entities.Donation;
import com.DevSprint.voluntrix_backend.Entities.Donor;
import com.DevSprint.voluntrix_backend.repositories.DonationRepository;
import com.DevSprint.voluntrix_backend.repositories.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;

    public Donation createDonation(String name, String email, String phoneNumber, Double amount) {
        Donor donor = donorRepository.findByEmail(email)
                .orElseGet(() -> donorRepository.save(new Donor(null, name, email, phoneNumber)));

        Donation donation = new Donation();
        donation.setAmount(amount);
        donation.setStatus("PENDING");
        donation.setTimestamp(LocalDateTime.now());
        donation.setDonor(donor);

        return donationRepository.save(donation);
    }
}
