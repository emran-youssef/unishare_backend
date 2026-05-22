package com.unishare.unishare.dtos.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProcessPaymentRequest {


    // Online or Cash
    @NotBlank(message = "Payment method is required")
    @Pattern(
            regexp = "^(ONLINE|CASH)$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Payment method must be ONLINE or CASH"
    )
    private String paymentMethod;

    // Only for online payments — null is valid for cash
    private String transactionRef;

}
