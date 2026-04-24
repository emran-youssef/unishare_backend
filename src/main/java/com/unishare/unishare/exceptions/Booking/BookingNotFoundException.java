package com.unishare.unishare.exceptions.Booking;

public class BookingNotFoundException extends RuntimeException{

    public BookingNotFoundException(Long id)
    {
        super("Booking with this id not found: "+ id);
    }

}
