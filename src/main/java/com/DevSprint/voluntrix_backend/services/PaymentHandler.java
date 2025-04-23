package com.DevSprint.voluntrix_backend.services;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

@Service
public class PaymentHandler {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Environment env;

    public String sendPaymentRequest() {
        String url = "https://sandbox.payhere.lk/pay/checkout";

        // Merchant and order details
        String merchantId = env.getProperty("MERCHANT_ID");
        String orderId = "ORD12345";
        String amount = "1000.00";
        String currency = "LKR";
        String secret = env.getProperty("SECRET_KEY"); 

        

        // Generate hash
        String hashString = merchantId + orderId + amount + currency + md5(secret);
        String hash = md5(hashString);

        // Create form data
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("merchant_id", merchantId);
        params.add("return_url", "https://yourwebsite.com/return");
        params.add("cancel_url", "https://yourwebsite.com/cancel");
        params.add("notify_url", "https://yourwebsite.com/notify");
        params.add("first_name", "John");
        params.add("last_name", "Doe");
        params.add("email", "johndoe@example.com");
        params.add("phone", "1234567890");
        params.add("address", "123 Street, City");
        params.add("city", "Colombo");
        params.add("country", "Sri Lanka");
        params.add("order_id", orderId);
        params.add("items", "Product Name");
        params.add("currency", currency);
        params.add("amount", amount);
        params.add("hash", hash);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // Send request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        System.out.println("Payment API Response: " + response.getBody());

        return response.getBody();
    }

    // Method to generate MD5 hash
    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return byteArrayToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    // Convert byte array to hex string
    private String byteArrayToHex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String hash = formatter.toString();
        formatter.close();
        return hash;
    }
}
