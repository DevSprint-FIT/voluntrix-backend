package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.services.PaymentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/payment")
public class PaymentController {

    @Autowired
    private PaymentHandler paymentHandler;

    @GetMapping("/process")
    public String processPayment() {
        System.out.println("Processing payment...");
        return paymentHandler.sendPaymentRequest();
    }
}
