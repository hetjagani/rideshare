package com.rideshare.rating.exception;

public class ForbiddenException extends Exception {
    public ForbiddenException() {
        super("Access Denied: Forbidden");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
