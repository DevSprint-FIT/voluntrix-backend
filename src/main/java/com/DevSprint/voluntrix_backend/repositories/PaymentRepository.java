package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.PaymentEntity;
import com.DevSprint.voluntrix_backend.enums.PaymentStatus;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String>{

    List<PaymentEntity> findByStatusAndReceivedTimestampBefore(PaymentStatus status, LocalDateTime timestamp);
    Optional<PaymentEntity> findByOrderId(String orderId);
}
