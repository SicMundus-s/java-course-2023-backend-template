package edu.java.bot.controller;

import edu.java.core.exception.ApiErrorResponse;
import edu.java.core.exception.BadRequestException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ApiErrorResponse> handleConflict(BadRequestException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
            "Некорректные параметры запроса",
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
        return ResponseEntity.badRequest().body(response);
    }

}
