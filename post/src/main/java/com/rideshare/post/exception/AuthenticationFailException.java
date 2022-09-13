package com.rideshare.post.exception;

import java.io.IOException;

public class AuthenticationFailException extends IOException {
    public AuthenticationFailException(String message) {
        super(message);
    }
}
