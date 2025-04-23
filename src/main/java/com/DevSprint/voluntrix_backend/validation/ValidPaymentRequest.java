package com.DevSprint.voluntrix_backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PaymentRequestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPaymentRequest {
    String message() default "Invalid payment request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
