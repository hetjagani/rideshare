package com.rideshare.ride.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LogManager.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception ex, WebRequest r) {
        logger.debug("Got Exception: " + ex.getClass().toString());
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> forbiddenExceptionHandler(ForbiddenException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> emptyResultDataException(EmptyResultDataAccessException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                "requested entity not found",
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundException(EntityNotFoundException ex, WebRequest r) {
        String uri = ((ServletWebRequest)r).getRequest().getRequestURI();
        ErrorResponse errorResponse = new ErrorResponse(new Date(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                uri );

        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }
}
