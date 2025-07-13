package com.DevSprint.voluntrix_backend.utils;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.PayHereNotificationDto;
import com.DevSprint.voluntrix_backend.dtos.PaymentRequestDto;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.PaymentEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.enums.PaymentStatus;

@Component
public class PaymentMapper {
    
    // Maps a PaymentRequestDto to a PaymentEntity
    public PaymentEntity toEntity(PaymentRequestDto dto, SponsorEntity sponsor, VolunteerEntity  volunteer, EventEntity event) {
        PaymentEntity payment = new PaymentEntity();
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

        return payment;
    }

    // Updates a PaymentEntity with notification data
    public void updateEntityFromNotification(PaymentEntity payment, PayHereNotificationDto dto) {
        payment.setPaymentId(dto.getPayment_id());
        payment.setStatusCode(dto.getStatus_code());
        payment.setStatusMessage(dto.getStatus_message());
        payment.setMethod(dto.getMethod());
        payment.setStatus("2".equals(dto.getStatus_code()) ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
    }

    // Maps request parameters to PayHereNotificationDto
    public PayHereNotificationDto toNotificationDto(Map<String, String> params) {
        PayHereNotificationDto dto = new PayHereNotificationDto();
        dto.setMerchant_id(params.get("merchant_id"));
        dto.setOrder_id(params.get("order_id"));
        dto.setPayment_id(params.get("payment_id"));
        dto.setPayhere_amount(params.get("payhere_amount"));
        dto.setPayhere_currency(params.get("payhere_currency"));
        dto.setStatus_code(params.get("status_code"));
        dto.setMd5sig(params.get("md5sig"));
        dto.setStatus_message(params.get("status_message"));
        dto.setMethod(params.get("method"));

        return dto;
    }
}
