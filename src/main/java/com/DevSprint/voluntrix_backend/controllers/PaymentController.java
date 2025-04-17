package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.DonationRequest;
import com.DevSprint.voluntrix_backend.Entities.Donation;
import com.DevSprint.voluntrix_backend.services.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final DonationService donationService;

    @PostMapping("/donate")
    public ResponseEntity<Donation> donate(@RequestBody DonationRequest request) {
        Donation donation = donationService.createDonation(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getAmount()
        );

        // You can later call your payment gateway here, or return payment URL/token
        return ResponseEntity.ok(donation);
    }
}
