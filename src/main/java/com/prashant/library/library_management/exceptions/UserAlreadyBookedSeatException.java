package com.prashant.library.library_management.exceptions;

public class UserAlreadyBookedSeatException  extends RuntimeException{

    public UserAlreadyBookedSeatException(String message)
    {
        super(message);
    }
}
