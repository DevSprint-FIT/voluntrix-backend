package com.DevSprint.voluntrix_backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.PayHereNotificationDTO;
import com.DevSprint.voluntrix_backend.dtos.PaymentRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.PaymentResponseDTO;
import com.DevSprint.voluntrix_backend.dtos.PaymentStatusResponseDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.PaymentEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.enums.TransactionType;
import com.DevSprint.voluntrix_backend.exceptions.PaymentNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.PaymentRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.utils.PaymentMapper;

import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EventRepository eventRepository;
    private final SponsorRepository sponsorRepository;
    private final VolunteerRepository volunteerRepository;
    private final PaymentMapper paymentMapper;

    @Value("${PAYHERE_MERCHANT_ID}")
    private String merchantId;

    @Value("${PAYHERE_MERCHANT_SECRET}")
    private String merchantSecret;

    private String md5(String input) {
        return generateHash(input);
    }

    public String generateHashForPayment(PaymentRequestDTO paymentRequest) {
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

        return expectedMd5sig.equalsIgnoreCase(receivedMd5sig);
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

    // save initial transaction
    public PaymentResponseDTO createPendingPayment(PaymentRequestDTO dto) {
        // Validate sponsor
        SponsorEntity sponsor = null;
        if (dto.getSponsorId() != null) {
            sponsor = sponsorRepository.findById(dto.getSponsorId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid sponsor ID"));
        }
        
        // Validate volunteer
        VolunteerEntity volunteer = null;
        if (dto.getVolunteerId() != null) {
            volunteer = volunteerRepository.findById(dto.getVolunteerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid volunteer ID"));
        }

        // Validat event
        EventEntity event = null;
        if (dto.getEventId() != null){
            event = eventRepository.findById(dto.getEventId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));  
        }

        // Business rule: only sponsors can make sponsorships
        if (dto.getTransactionType() == TransactionType.SPONSORSHIP && sponsor == null) {
            throw new IllegalArgumentException("Only sponsors can make sponsorships");
        }

        PaymentEntity payment = paymentMapper.toEntity(dto, sponsor, volunteer, event);
        payment.setVolunteer(volunteer);
        paymentRepository.save(payment);

        String hash = generateHashForPayment(dto);
        return new PaymentResponseDTO(hash, merchantId);
    }

    public void saveTransaction(PayHereNotificationDTO dto) {
        PaymentEntity payment = paymentRepository.findById(dto.getOrder_id())
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order ID: " + dto.getOrder_id()));

        paymentMapper.updateEntityFromNotification(payment, dto);
        paymentRepository.save(payment);
    }

    public PaymentStatusResponseDTO getPaymentStatusByOrderId(String orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order ID: " + orderId));

        return new PaymentStatusResponseDTO(orderId, payment.getStatus());
    }
    
}
