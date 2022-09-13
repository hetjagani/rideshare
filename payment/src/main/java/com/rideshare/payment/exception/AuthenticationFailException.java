package com.rideshare.payment.exception;

import java.io.IOException;

public class AuthenticationFailException extends IOException {
    public AuthenticationFailException(String message) {
        super(message);
    }
}
