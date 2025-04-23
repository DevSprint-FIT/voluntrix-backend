package com.DevSprint.voluntrix_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.PayHereNotificationDto;
import com.DevSprint.voluntrix_backend.dtos.PaymentRequestDto;
import com.DevSprint.voluntrix_backend.dtos.PaymentResponseDto;
import com.DevSprint.voluntrix_backend.entities.Event;
import com.DevSprint.voluntrix_backend.entities.Payment;
import com.DevSprint.voluntrix_backend.entities.Sponsor;
import com.DevSprint.voluntrix_backend.entities.Volunteer;
import com.DevSprint.voluntrix_backend.enums.PaymentStatus;
import com.DevSprint.voluntrix_backend.enums.TransactionType;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.PaymentRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EventRepository eventRepository;
    private final SponsorRepository sponsorRepository;
    private final VolunteerRepository volunteerRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, EventRepository eventRepository, SponsorRepository sponsorRepository, VolunteerRepository volunteerRepository) {
        this.paymentRepository = paymentRepository;
        this.eventRepository = eventRepository;
        this.sponsorRepository = sponsorRepository;
        this.volunteerRepository = volunteerRepository;
    }

    @Value("${PAYHERE_MERCHANT_ID}")
    private String merchantId;

    @Value("${PAYHERE_MERCHANT_SECRET}")
    private String merchantSecret;

    private String md5(String input) {
        return generateHash(input);
    }

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
    public PaymentResponseDto createPendingPayment(PaymentRequestDto dto) {
        // Validate sponsor
        Sponsor sponsor = null;
        if (dto.getSponsorId() != null) {
            sponsor = sponsorRepository.findById(dto.getSponsorId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid sponsor ID"));
        }
        
        // Validate volunteer
        Volunteer volunteer = null;
        if (dto.getVolunteerId() != null) {
            volunteer = volunteerRepository.findById(dto.getVolunteerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid volunteer ID"));
        }

        // Validat event
        Event event = null;
        if (dto.getEventId() != null){
            event = eventRepository.findById(dto.getEventId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));  
        }

        // Business rule: only sponsors can make sponsorships
        if (dto.getTransactionType() == TransactionType.SPONSORSHIP && sponsor == null) {
            throw new IllegalArgumentException("Only sponsors can make sponsorships");
        }

        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        payment.setAmount(Double.parseDouble(dto.getAmount()));
        payment.setCurrency(dto.getCurrency());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setReceivedTimestamp(LocalDateTime.now());
        payment.setEmail(dto.getEmail());
        payment.setAnonymous(dto.isAnonymous());
        payment.setTransactionType(dto.getTransactionType());
        payment.setSponsor(sponsor);
        payment.setEvent(event);
        payment.setVolunteer(volunteer);

        paymentRepository.save(payment);

        String hash = generateHashForPayment(dto);
        return new PaymentResponseDto(hash, merchantId);
    }

    public void saveTransaction(PayHereNotificationDto dto) {
        Payment payment = paymentRepository.findById(dto.getOrder_id())
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentId(dto.getPayment_id());
        payment.setStatusCode(dto.getStatus_code());
        payment.setStatusMessage(dto.getStatus_message());
        payment.setMethod(dto.getMethod());
        payment.setStatus("2".equals(dto.getStatus_code()) ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);

        paymentRepository.save(payment);
    }
    
}
