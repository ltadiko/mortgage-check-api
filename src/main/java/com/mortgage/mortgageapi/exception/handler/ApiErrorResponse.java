package com.mortgage.mortgageapi.exception.handler;


import java.time.LocalDateTime;

public record ApiErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp,
        String path,
        Object errors
) {

}