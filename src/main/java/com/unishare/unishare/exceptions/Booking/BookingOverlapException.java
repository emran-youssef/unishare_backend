package com.unishare.unishare.exceptions.Booking;

public class BookingOverlapException extends RuntimeException {

    public BookingOverlapException(String message)
    {
        super(message);
    }

    public BookingOverlapException(){
        super("These dates are already booked for this listing");
    }
}
