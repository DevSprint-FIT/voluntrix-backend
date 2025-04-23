package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.Entities.Donation;
import com.DevSprint.voluntrix_backend.Entities.Donor;
import com.DevSprint.voluntrix_backend.exceptions.InvalidInputException;
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

        // ✅ Validation
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be empty");
        }

        if (amount == null || amount <= 0) {
            throw new InvalidInputException("Amount must be greater than 0");
        }

        // ✅ Create or fetch donor
        Donor donor = donorRepository.findByEmail(email)
                .orElseGet(() -> donorRepository.save(new Donor(null, name, email, phoneNumber)));

        // ✅ Create donation
        Donation donation = new Donation();
        donation.setAmount(amount);
        donation.setStatus("PENDING");
        donation.setTimestamp(LocalDateTime.now());
        donation.setDonor(donor);

        return donationRepository.save(donation);
    }
}
