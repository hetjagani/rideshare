package com.rideshare.auth.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
    }

    @Override
    public String getMessage() {
        return "User Already Exist";
    }
}
