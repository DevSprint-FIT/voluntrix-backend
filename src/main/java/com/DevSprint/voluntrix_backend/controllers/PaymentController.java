package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.PaymentRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.PaymentResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.PaymentStatusResponseDTO;
import com.DevSprint.voluntrix_backend.services.PaymentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/start")
    public ResponseEntity<PaymentResponseDTO> startPayment(@RequestBody @Valid PaymentRequestDTO paymentRequest) {
        return ResponseEntity.ok(paymentService.createPendingPayment(paymentRequest));
    } 

    @GetMapping("/status/{orderId}")
    public ResponseEntity<PaymentStatusResponseDTO> getPaymentStatus(@PathVariable String orderId) {
        PaymentStatusResponseDTO statusDto = paymentService.getPaymentStatusByOrderId(orderId);
        return ResponseEntity.ok(statusDto);
    }
}