package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.PayHereNotificationDTO;
import com.DevSprint.voluntrix_backend.dtos.PaymentRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.PaymentResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.PaymentStatusResponseDTO;
import com.DevSprint.voluntrix_backend.exceptions.PaymentVerificationException;
import com.DevSprint.voluntrix_backend.services.PaymentService;
import com.DevSprint.voluntrix_backend.utils.PaymentMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PostMapping("/start")
    public ResponseEntity<PaymentResponseDTO> startPayment(@RequestBody @Valid PaymentRequestDTO paymentRequest) {
        return ResponseEntity.ok(paymentService.createPendingPayment(paymentRequest));
    } 

    @PostMapping("/notify")
    public ResponseEntity<String> notifyPayment(@RequestParam Map<String, String> params) {
        boolean isValid = paymentService.verifyPayment(params);

        if (!isValid) {
            throw new PaymentVerificationException("Payment signature verification failed");
        }

        PayHereNotificationDTO dto = paymentMapper.toNotificationDto(params);
        paymentService.saveTransaction(dto);
        
        return ResponseEntity.ok("Transaction saved succesfully.");
    } 

    @GetMapping("/status/{orderId}")
    public ResponseEntity<PaymentStatusResponseDTO> getPaymentStatus(@PathVariable String orderId) {
        PaymentStatusResponseDTO statusDto = paymentService.getPaymentStatusByOrderId(orderId);
        return ResponseEntity.ok(statusDto);
    }
}