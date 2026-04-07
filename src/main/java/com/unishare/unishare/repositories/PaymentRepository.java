package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    //fetch the payment on a given booking, used to check the payment status
    Optional<Payment> findByBookingId(Long bookingId);

    //used before creating a payment to prevent a duplicate, A booking can have only one payment
    boolean existsByBookingId(Long bookingId);
}
