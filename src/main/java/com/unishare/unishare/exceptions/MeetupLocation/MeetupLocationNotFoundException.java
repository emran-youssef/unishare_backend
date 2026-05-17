package com.unishare.unishare.exceptions.MeetupLocation;

public class MeetupLocationNotFoundException extends RuntimeException {
    public MeetupLocationNotFoundException(Long id) {
        super("Meetup location not found: " + id);
    }
}
