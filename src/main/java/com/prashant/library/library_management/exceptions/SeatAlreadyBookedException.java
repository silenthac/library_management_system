package com.prashant.library.library_management.exceptions;

public class SeatAlreadyBookedException extends RuntimeException {

    public SeatAlreadyBookedException(String message)
    {
        super(message);
    }
}
