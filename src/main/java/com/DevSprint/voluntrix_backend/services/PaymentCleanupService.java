package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.enums.PaymentStatus;
import com.DevSprint.voluntrix_backend.repositories.PaymentRepository;
import com.DevSprint.voluntrix_backend.entities.PaymentEntity;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PaymentCleanupService {
    
    private final PaymentRepository paymentRepository;

    public PaymentCleanupService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Transactional
    public void cleanUpExpiredPayments() {
        LocalDateTime expirationThreshhold = LocalDateTime.now().minusMinutes(15);
        List<PaymentEntity> oldPendingPayments = paymentRepository.findByStatusAndReceivedTimestampBefore(
            PaymentStatus.PENDING, expirationThreshhold
        );

        for (PaymentEntity payment : oldPendingPayments) {
            payment.setStatus(PaymentStatus.EXPIRED);
        }

        paymentRepository.saveAll(oldPendingPayments);

        if (!oldPendingPayments.isEmpty()) {
            System.out.println("Marked " + oldPendingPayments.size() + " payments as expired.");
        }
    }

}
