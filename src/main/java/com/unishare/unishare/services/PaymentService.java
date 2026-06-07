package com.unishare.unishare.services;

import com.unishare.unishare.dtos.payment.PaymentDto;
import com.unishare.unishare.dtos.payment.ProcessPaymentRequest;
import com.unishare.unishare.entities.Booking;
import com.unishare.unishare.entities.Payment;
import com.unishare.unishare.enums.BookingStatus;
import com.unishare.unishare.enums.PaymentMethod;
import com.unishare.unishare.enums.PaymentStatus;
import com.unishare.unishare.exceptions.Booking.BookingNotFoundException;
import com.unishare.unishare.exceptions.Payment.PaymentAlreadyExistsException;
import com.unishare.unishare.exceptions.UnauthorizedException.UnauthorizedActionException;
import com.unishare.unishare.mappers.PaymentMapper;
import com.unishare.unishare.repositories.BookingRepository;
import com.unishare.unishare.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentDto processPayment(String callerEmail, ProcessPaymentRequest request, Long bookingId)
    {
        // 1. Load the booking
        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        // 2. Verify the caller is the renter
        if (!booking.getRenter().getUniversityEmail().equals(callerEmail))
            throw new UnauthorizedActionException("Only the renter can pay for this booking");

        // 3. One payment per booking — no duplicates
        if (paymentRepository.existsByBooking_Id(bookingId))
            throw new PaymentAlreadyExistsException("A payment already exists for this booking: " + bookingId);

        // 4. Payment only allowed on CONFIRMED bookings
        if (booking.getStatus() != BookingStatus.CONFIRMED)
            throw new IllegalStateException("Payment is only allowed on CONFIRMED bookings. Current status: " + booking.getStatus());

        // 5. Determine payment method
        boolean isOnline = request.getPaymentMethod() == PaymentMethod.ONLINE;

        // 6A. call the helper method createPayment tp build a payment record
        var payment = createPayment(booking, request, isOnline);
        var saved = paymentRepository.save(payment);

        return paymentMapper.toPaymentDto(saved);

    }

    public PaymentDto getPaymentByBookingId(String callerEmail, Long bookingId) {

        var payment = paymentRepository.findByBooking_Id(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        boolean isRenter = payment.getBooking().getRenter()
                .getUniversityEmail().equals(callerEmail);

        boolean isOwner = payment.getBooking().getListing().getOwner()
                .getUniversityEmail().equals(callerEmail);

        if (!isRenter && !isOwner)
            throw new UnauthorizedActionException("You are not involved in this booking");

        return paymentMapper.toPaymentDto(payment);
    }


    private Payment createPayment(Booking booking, ProcessPaymentRequest request, boolean isOnline) {
        return Payment.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .paymentMethod(request.getPaymentMethod())
                .status(isOnline ? PaymentStatus.PAID : PaymentStatus.PENDING)
                .paidAt(isOnline ? LocalDateTime.now() : null)
                .build();
    }


}
