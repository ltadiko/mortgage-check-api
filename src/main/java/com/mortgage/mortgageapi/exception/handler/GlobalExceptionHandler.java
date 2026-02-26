package com.mortgage.mortgageapi.exception.handler;

import com.mortgage.mortgageapi.exception.BusinessRuleViolationException;
import com.mortgage.mortgageapi.exception.RateNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // -------------------- Business Rule Violations (422) --------------------
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRuleViolation(
            BusinessRuleViolationException ex,
            HttpServletRequest request) {

        log.warn("Business rule violation at {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getViolations());

        Map<String, Object> details = Map.of("violations", ex.getViolations());
        return buildResponse(HttpStatus.valueOf(422), "Mortgage request failed business rules", request, details);
    }

    // -------------------- Validation Errors (400) --------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationMapping(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));

        log.warn("Validation failed for request at {} {}: {}", request.getMethod(), request.getRequestURI(), validationErrors);
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed", request, validationErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getConstraintViolations().forEach(v ->
                validationErrors.put(v.getPropertyPath().toString(), v.getMessage()));

        log.warn("Constraint violation at {} {}: {}", request.getMethod(), request.getRequestURI(), validationErrors);
        return buildResponse(HttpStatus.BAD_REQUEST, "Constraint Violation", request, validationErrors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.warn("Missing request parameter '{}' at {} {}", ex.getParameterName(), request.getMethod(), request.getRequestURI());
        return buildResponse(HttpStatus.BAD_REQUEST,
                String.format("Missing required parameter: %s", ex.getParameterName()), request, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed JSON or missing body at {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON or missing request body.", request, null);
    }

    @ExceptionHandler(RateNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRateNotFound(RateNotFoundException ex, HttpServletRequest request) {
        log.warn("Rate not found at {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("No resource found at {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Resource not found", request, null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String message = String.format("Request method '%s' is not supported. Supported methods: %s", ex.getMethod(), ex.getSupportedHttpMethods());
        log.warn("Method not supported at {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getMethod());
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, message, request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception at {} {}", request.getMethod(), request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.", request, null);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request, Object details) {
        ApiErrorResponse error = new ApiErrorResponse(
                status.value(),
                message,
                LocalDateTime.now(),
                request.getRequestURI(),
                details
        );
        return new ResponseEntity<>(error, status);
    }
}