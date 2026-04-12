package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    //fetch the payment on a given booking, used to check the payment status
    Optional<Payment> findByBooking_Id(Long bookingId);

    //used before creating a payment to prevent a duplicate, A booking can have only one payment
    boolean existsByBooking_Id(Long bookingId);


}
