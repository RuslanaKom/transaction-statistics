package com.n26.challenge.statistics.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class HttpExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                          HttpStatus status, WebRequest request) {
        String errorMessage = createErrorMessage(ex);
        status = getHttpStatus(ex, status);
        return ResponseEntity.status(status)
                .body(errorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex.getCause() instanceof InvalidFormatException) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(String.format("Invalid format of value: %s ", ((InvalidFormatException) ex.getCause()).getValue()));
        }
        return ResponseEntity.status(status)
                .build();
    }

    private HttpStatus getHttpStatus(MethodArgumentNotValidException ex, HttpStatus status) {
        if (ex.getBindingResult()
                .getAllErrors()
                .size() == 1) {
            status = Arrays.stream(ex.getBindingResult()
                    .getAllErrors()
                    .get(0)
                    .getArguments())
                    .filter(arg -> arg instanceof HttpStatus)
                    .findFirst()
                    .map(HttpStatus.class::cast)
                    .orElse(HttpStatus.BAD_REQUEST);
        }
        return status;
    }

    private String createErrorMessage(MethodArgumentNotValidException ex) {
        return "Invalid parameters: " +
                ex.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(error -> ((FieldError) error).getField() + " -> " + error.getDefaultMessage())
                        .collect(Collectors.joining(", ")) + ".";
    }
}
