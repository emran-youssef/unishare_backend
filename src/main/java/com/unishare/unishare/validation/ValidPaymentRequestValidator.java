package com.unishare.unishare.validation;


import com.unishare.unishare.dtos.payment.ProcessPaymentRequest;
import com.unishare.unishare.enums.PaymentMethod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPaymentRequestValidator
        implements ConstraintValidator<ValidPaymentRequest, ProcessPaymentRequest> {

    @Override
    public boolean isValid(ProcessPaymentRequest request, ConstraintValidatorContext context) {

        //If payment method was cash, return true
        if(request == null || request.getPaymentMethod() == null)   return true;

        boolean isOnline = request.getPaymentMethod() == PaymentMethod.ONLINE;

        if (!isOnline) return true; // CASH — no card fields needed, always valid

        // ONLINE — all three card fields are required
        boolean valid = true;

        if(request.getCardNumber() == null || request.getCardNumber().isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Card number is required for online payment")
                    .addPropertyNode("cardNumber")
                    .addConstraintViolation();
            valid = false;
        }

        if (request.getCvv() == null ||request.getCvv().isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("CVV is required for online payment")
                    .addPropertyNode("cvv")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;


    }
}
