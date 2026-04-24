package com.unishare.unishare.exceptions.UnauthorizedException;

public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
