package com.rideshare.auth.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI().toString();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExistsExceptionHandler(UserAlreadyExistsException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI().toString();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestExceptionHandler(BadRequestException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI().toString();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<?> userDoesNotExistExceptionHandler(UserDoesNotExistException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI().toString();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsExceptionHandler(BadCredentialsException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI().toString();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> disabledUserExceptionHandler(DisabledException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI().toString();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> expiredJwtExceptionExceptionHandler(ExpiredJwtException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI().toString();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<?> unsupportedJwtExceptionExceptionHandler(UnsupportedJwtException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI().toString();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> malformedJwtExceptionExceptionHandler(MalformedJwtException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI().toString();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> signatureExceptionExceptionHandler(SignatureException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI().toString();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
