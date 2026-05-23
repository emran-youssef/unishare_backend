package com.unishare.unishare.dtos.payment;

import com.unishare.unishare.enums.PaymentMethod;
import com.unishare.unishare.validation.ValidPaymentRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@ValidPaymentRequest
public class ProcessPaymentRequest {

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits")
    private String cardNumber;

    @Pattern(regexp = "\\d{3}", message = "CVV must be exactly 3 digits")
    private String cvv;


}
