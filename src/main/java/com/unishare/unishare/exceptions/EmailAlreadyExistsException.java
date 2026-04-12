// Thrown when a registration attempt uses a university email that already exists in the system
package com.unishare.unishare.exceptions;


public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
