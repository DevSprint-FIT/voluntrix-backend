package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.dtos.analytics.MonthlyDonationData;
import com.DevSprint.voluntrix_backend.entities.PaymentEntity;
import com.DevSprint.voluntrix_backend.enums.PaymentStatus;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String>{

    List<PaymentEntity> findByStatusAndReceivedTimestampBefore(PaymentStatus status, LocalDateTime timestamp);
    Optional<PaymentEntity> findByOrderId(String orderId);

    @Query("SELECT MONTH(p.receivedTimestamp) as month, SUM(p.amount) as total " +
            "FROM PaymentEntity p WHERE p.volunteer.id = :volunteerId " + 
            "AND p.transactionType = 'DONATION' AND p.status = 'SUCCESS' " + 
            "GROUP BY MONTH(p.receivedTimestamp) ORDER BY MONTH(p.receivedTimestamp)")
    List<MonthlyDonationData> findMonthlyDonationsByVolunteerAndYear(@Param("volunteerId") Long volunteerId, @Param("year") int year);

    @Query(
        "SELECT SUM(p.amount) as totalDonations " +
        "FROM PaymentEntity p WHERE p.volunteer.id = :volunteerId " + 
        "AND p.transactionType = 'DONATION' AND p.status = 'SUCCESS'")
    Double sumTotalDonationsByVolunteer(@Param("volunteerId") Long volunteerId);
}
