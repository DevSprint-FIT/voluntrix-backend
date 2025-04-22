package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
public class Payment {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "status")
    private String status;

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

    @Column(name = "user_type")
    private String userType;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "is_anonymous")
    private boolean isAnonymous;
}
