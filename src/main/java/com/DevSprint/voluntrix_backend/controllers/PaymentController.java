package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.PaymentRequestDto;
import com.DevSprint.voluntrix_backend.dtos.PaymentResponseDto;
import com.DevSprint.voluntrix_backend.services.PaymentService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/start")
    public PaymentResponseDto startPayment(@RequestBody PaymentRequestDto paymentRequest) {
        String hash = paymentService.generateHashForPayment(paymentRequest);
        return new PaymentResponseDto(hash, paymentService.getMerchantId());
    } 

    @PostMapping("/notify")
    public String notifyPayment(@RequestParam Map<String, String> payload) {
        boolean valid = paymentService.verifyPayment(payload);
        if (valid) {
            System.out.println("Paymnet Verified");
        } else {
            System.out.println("Payment is not verified");
        }
        return valid ? "OK" : "FAILED"; 
    }

}