package com.mortgage.mortgageapi.exception.handler;

import com.mortgage.mortgageapi.exception.BusinessRuleViolationException;
import com.mortgage.mortgageapi.exception.RateNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/test");
    }

    @Test
    @DisplayName("Should handle BusinessRuleViolationException")
    void shouldHandleBusinessRuleViolationException() {
        var ex = new BusinessRuleViolationException(List.of("Loan > Income"));
        var response = handler.handleBusinessRuleViolation(ex, request).getBody();

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(422);
        assertThat(response.message()).contains("Mortgage request failed business rules");

        @SuppressWarnings("unchecked")
        var errorsMap = (Map<String, Object>) response.errors();
        assertThat(errorsMap).containsKey("violations");
    }

    @Test
    @DisplayName("Should handle RateNotFoundException")
    void shouldHandleRateNotFoundException() {
        var ex = new RateNotFoundException("Rate missing");
        var response = handler.handleRateNotFound(ex, request).getBody();

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(404);
        assertThat(response.message()).contains("Rate missing");
    }


    @Test
    @DisplayName("Should handle general Exception")
    void shouldHandleGeneralException() {
        var ex = new RuntimeException("oops");
        var response = handler.handleGeneralException(ex, request).getBody();

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(500);
        assertThat(response.message()).contains("unexpected error");
    }
}