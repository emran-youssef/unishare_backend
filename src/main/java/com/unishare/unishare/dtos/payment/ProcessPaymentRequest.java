package com.unishare.unishare.dtos.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProcessPaymentRequest {


    // Online or Cash
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    // Only for online payments — null is valid for cash
    private String transactionRef;

}
