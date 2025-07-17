package com.DevSprint.voluntrix_backend.validation;

import com.DevSprint.voluntrix_backend.dtos.PaymentRequestDTO;
import com.DevSprint.voluntrix_backend.enums.UserType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PaymentRequestValidator implements ConstraintValidator<ValidPaymentRequest, PaymentRequestDTO> {

    @Override
    public boolean isValid(PaymentRequestDTO dto, ConstraintValidatorContext context) {
        boolean valid = true;

        // Disable default message
        context.disableDefaultConstraintViolation();

        // Validate userType related logic
        if (dto.getUserType() == UserType.SPONSOR) {
            if (dto.getSponsorId() == null) {
                context.buildConstraintViolationWithTemplate("Sponsor ID is required for SPONSOR userType")
                        .addPropertyNode("sponsorId").addConstraintViolation();
                valid = false;
            }
            if (dto.getVolunteerId() != null) {
                context.buildConstraintViolationWithTemplate("VOLUNTEER ID must not be provided for SPONSOR")
                        .addPropertyNode("volunteerId").addConstraintViolation();
                valid = false;
            }
        } else if (dto.getUserType() == UserType.VOLUNTEER) {
            if (dto.getVolunteerId() == null) {
                context.buildConstraintViolationWithTemplate("Volunteer ID is required for VOLUNTEER userType")
                        .addPropertyNode("volunteerId").addConstraintViolation();
                valid = false;
            }
            if (dto.getSponsorId() != null) {
                context.buildConstraintViolationWithTemplate("SPONSOR ID must not be provided for VOLUNTEER")
                        .addPropertyNode("sponsorId").addConstraintViolation();
                valid = false;
            }
        }

        // Ensure eventId is present for donation/sponsorship
        if (dto.getTransactionType() != null) {
            if (dto.getEventId() == null) { 
                context.buildConstraintViolationWithTemplate("Event ID is required for donation or sponsorship")
                        .addPropertyNode("eventId").addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }
}
