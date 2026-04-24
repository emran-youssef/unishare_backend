package com.unishare.unishare.exceptions.User;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message)
    {
        super(message);
    }
}
