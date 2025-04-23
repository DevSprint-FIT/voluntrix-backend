package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.PayHereNotificationDto;
import com.DevSprint.voluntrix_backend.dtos.PaymentRequestDto;
import com.DevSprint.voluntrix_backend.dtos.PaymentResponseDto;
import com.DevSprint.voluntrix_backend.services.PaymentService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
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

            PayHereNotificationDto dto = new PayHereNotificationDto();
            dto.setMerchant_id(params.get("merchant_id"));
            dto.setOrder_id(params.get("order_id"));
            dto.setPayment_id(params.get("payment_id"));
            dto.setStatus_code(params.get("status_code"));
            dto.setMd5sig(params.get("md5sig"));
            dto.setStatus_message(params.get("status_message"));
            dto.setPayhere_amount(params.get("payhere_amount"));
            dto.setPayhere_currency(params.get("payhere_currency"));
            dto.setMethod(params.get("method"));

            paymentService.saveTransaction(dto);
            return ResponseEntity.ok("Transaction saved succesfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal server error.");
        }
    } 

}