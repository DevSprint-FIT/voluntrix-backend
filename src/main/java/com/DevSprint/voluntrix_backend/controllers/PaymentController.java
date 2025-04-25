package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.PayHereNotificationDto;
import com.DevSprint.voluntrix_backend.dtos.PaymentRequestDto;
import com.DevSprint.voluntrix_backend.dtos.PaymentResponseDto;
import com.DevSprint.voluntrix_backend.dtos.PaymentStatusResponseDto;
import com.DevSprint.voluntrix_backend.services.PaymentService;
import com.DevSprint.voluntrix_backend.utils.PaymentMapper;

import jakarta.validation.Valid;

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
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    public PaymentController(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }

    @PostMapping("/start")
    public ResponseEntity<PaymentResponseDto> startPayment(@RequestBody @Valid PaymentRequestDto paymentRequest) {
        return ResponseEntity.ok(paymentService.createPendingPayment(paymentRequest));
    } 

    @PostMapping("/notify")
    public ResponseEntity<String> notifyPayment(@RequestParam Map<String, String> params) {
        try{
            boolean isValid = paymentService.verifyPayment(params);

            if (!isValid) {
                return ResponseEntity.badRequest().body("Payment verification failed.");
            }

            PayHereNotificationDto dto = paymentMapper.toNotificationDto(params);
            paymentService.saveTransaction(dto);
            
            return ResponseEntity.ok("Transaction saved succesfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error.");
        }
    } 

    @GetMapping("/status/{orderId}")
    public ResponseEntity<PaymentStatusResponseDto> getPaymentStatus(@PathVariable String orderId) {
        PaymentStatusResponseDto statusDto = paymentService.getPaymentStatusByOrderId(orderId);
        return ResponseEntity.ok(statusDto);
    }

}