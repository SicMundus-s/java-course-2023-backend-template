package edu.java.scrapper.controller;

import edu.java.core.exception.ApiErrorResponse;
import edu.java.core.exception.BadRequestException;
import edu.java.core.exception.NotFoundException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> badRequestException(BadRequestException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
            "Invalid request parameters",
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> notFoundException(NotFoundException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
            ex.getMessage(),
            String.valueOf(HttpStatus.NOT_FOUND.value()),
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
