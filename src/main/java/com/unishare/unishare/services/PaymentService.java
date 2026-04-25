package com.unishare.unishare.services;

import com.unishare.unishare.dtos.payment.PaymentDto;
import com.unishare.unishare.dtos.payment.ProcessPaymentRequest;
import com.unishare.unishare.entities.Booking;
import com.unishare.unishare.entities.Payment;
import com.unishare.unishare.enums.BookingStatus;
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
    public PaymentDto processPayment(ProcessPaymentRequest request, Long bookingId)
    {
        // load the booking
        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new BookingNotFoundException(bookingId));

        // 2. Verify the caller is the renter
        String callerEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        if(!booking.getRenter().getUniversityEmail().equals(callerEmail))
            throw new UnauthorizedActionException("Only the renter can pay for this booking");

        // 3. One payment per booking — no duplicates
        if(paymentRepository.existsByBooking_Id(bookingId))
            throw new PaymentAlreadyExistsException("A payment already exist for this booking: "+ bookingId);


        // 4. Only PENDING bookings can be paid
        if (booking.getStatus() != BookingStatus.PENDING)
            throw new IllegalStateException("Payment is only allowed on PENDING bookings. Current status: "
                    + booking.getStatus());


        // 5. Determine payment method
        boolean isOnline = request.getPaymentMethod()
                .equalsIgnoreCase("ONLINE");

        // 6. call the helper method createPayment tp build a payment record
        var payment = createPayment(booking, request, isOnline);

        // 7. Online confirms the booking immediately, Cash leaves it pending until the owner confirm it
        if(isOnline) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
        }

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toPaymentDto(saved);

    }

    public PaymentDto getPaymentByBookingId(Long bookingId) {

        Payment payment = paymentRepository.findByBooking_Id(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        return paymentMapper.toPaymentDto(payment);
    }

    private Payment createPayment(Booking booking, ProcessPaymentRequest request, boolean isOnline) {
        return Payment.builder()
                .booking(booking)
                .amount(booking.getTotalPrice())
                .paymentMethod(request.getPaymentMethod().toUpperCase())
                .transactionRef(request.getTransactionRef())
                .status(isOnline ? PaymentStatus.PAID : PaymentStatus.PENDING)
                .paidAt(isOnline ? LocalDateTime.now() : null)
                .build();
    }


}
