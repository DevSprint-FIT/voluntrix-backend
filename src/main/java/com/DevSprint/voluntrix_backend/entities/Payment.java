package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.DevSprint.voluntrix_backend.enums.PaymentStatus;
import com.DevSprint.voluntrix_backend.enums.TransactionType;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "payment_id")
    private String paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "status_message")
    private String statusMessage;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "method")
    private String method;

    @Column(name = "email")
    private String email;

    @Column(name = "received_timestamp")
    private LocalDateTime receivedTimestamp;

    @ManyToOne
    @JoinColumn(name = "sponsor_id")
    private Sponsor sponsor;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "is_anonymous")
    private boolean isAnonymous;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;
}
