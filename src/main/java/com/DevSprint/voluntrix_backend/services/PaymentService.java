package com.DevSprint.voluntrix_backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.DevSprint.voluntrix_backend.dtos.PaymentRequestDto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Map;

@Service
public class PaymentService {

    @Value("${PAYHERE_MERCHANT_ID}")
    private String merchantId;

    @Value("${PAYHERE_MERCHANT_SECRET}")
    private String merchantSecret;

    public String generateHashForPayment(PaymentRequestDto paymentRequest) {
        return generateHash(merchantId + 
            paymentRequest.getOrderId() + 
            paymentRequest.getAmount() + 
            paymentRequest.getCurrency() + 
            md5(merchantSecret)
        );
    }

    public boolean verifyPayment(Map<String, String> payload) {
        String merchantIdFromPayload = payload.get("merchant_id");
        String orderId = payload.get("order_id");
        String amount = payload.get("payhere_amount");
        String currency = payload.get("payhere_currency");
        String statusCode = payload.get("status_code");
        String receivedMd5sig = payload.get("md5sig");

        String expectedMd5sig = generateHash(
            merchantIdFromPayload + orderId + amount + currency + statusCode + md5(merchantSecret)
        );

        return expectedMd5sig.equalsIgnoreCase(receivedMd5sig) && "2".equals(statusCode);
    }

    private String generateHash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available.", e);
        }
    }

    private String md5(String input) {
        return generateHash(input);
    }

    public String getMerchantId() {
        return merchantId;
    }
}
