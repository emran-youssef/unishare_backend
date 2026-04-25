package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.payment.PaymentDto;
import com.unishare.unishare.dtos.payment.ProcessPaymentRequest;
import com.unishare.unishare.services.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{bookingId}")
    public ResponseEntity<PaymentDto> processPayment(
            @PathVariable Long bookingId,
            @Valid @RequestBody ProcessPaymentRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.processPayment(request, bookingId));

    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<PaymentDto> getPayment(
            @PathVariable Long bookingId) {

        return ResponseEntity.
                ok(paymentService.getPaymentByBookingId(bookingId));
    }

}
