package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.Payment;
import com.DevSprint.voluntrix_backend.enums.PaymentStatus;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>{

    List<Payment> findByStatusAndReceivedTimestampBefore(PaymentStatus status, LocalDateTime timestamp);
}
