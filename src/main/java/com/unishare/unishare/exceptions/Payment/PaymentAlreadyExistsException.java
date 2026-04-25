// Thrown when a second payment is attempted on a booking that already has one
package com.unishare.unishare.exceptions.Payment;



public class PaymentAlreadyExistsException extends RuntimeException {
    public PaymentAlreadyExistsException(String message){
        super(message);
    }
}
