package com.mortgage.mortgageapi.controller;

import com.mortgage.mortgageapi.exception.BusinessRuleViolationException;
import com.mortgage.mortgageapi.exception.RateNotFoundException;
import com.mortgage.mortgageapi.model.MortgageCheckRequest;
import com.mortgage.mortgageapi.model.MortgageCheckResponse;
import com.mortgage.mortgageapi.service.MortgageCheckService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.mortgage.mortgageapi.util.ApiConstants.MORTGAGE_CHECK_ENDPOINT;

@WebMvcTest(MortgageCheckController.class)
class MortgageCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MortgageCheckService mortgageCheckService;

    @Test
    @DisplayName("Should return 200 and success response for valid request")
    void shouldReturnOkForValidRequest() throws Exception {
        var request = new MortgageCheckRequest(
                new BigDecimal("60000"), 30, new BigDecimal("200000"), new BigDecimal("250000"));

        var mockResponse = MortgageCheckResponse.success(new BigDecimal("898.09"));

        when(mortgageCheckService.checkMortgage(any())).thenReturn(mockResponse);

        mockMvc.perform(post(MORTGAGE_CHECK_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible").value(true)) // Verify if your field is 'feasible' or 'isFeasible'
                .andExpect(jsonPath("$.monthlyCosts").value(898.09));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when income is negative")
    void shouldReturnBadRequestForInvalidIncome() throws Exception {
        String jsonWithNegativeIncome = """
                {
                    "income": -100,
                    "maturityPeriod": 30,
                    "loanValue": 200000,
                    "homeValue": 250000
                }
                """;

        mockMvc.perform(post(MORTGAGE_CHECK_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithNegativeIncome))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 Bad Request when mandatory fields are missing")
    void shouldReturnBadRequestForMissingFields() throws Exception {
        String incompleteJson = """
                {
                    "maturityPeriod": 30,
                    "loanValue": 200000,
                    "homeValue": 250000
                }
                """;

        mockMvc.perform(post(MORTGAGE_CHECK_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 422 when loan exceeds income limit")
    void shouldReturn422ForBusinessRuleViolation() throws Exception {
        var request = new MortgageCheckRequest(
                new BigDecimal("50000"), 30, new BigDecimal("250000"), new BigDecimal("300000"));

        when(mortgageCheckService.checkMortgage(any()))
                .thenThrow(new BusinessRuleViolationException(List.of("Loan > income")));

        mockMvc.perform(post(MORTGAGE_CHECK_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.errors.violations[0]").value("Loan > income"));
    }

    @Test
    @DisplayName("Should return 404 when rate not found")
    void shouldReturn404WhenRateNotFound() throws Exception {
        var request = new MortgageCheckRequest(
                new BigDecimal("60000"), 40, new BigDecimal("200000"), new BigDecimal("250000"));

        when(mortgageCheckService.checkMortgage(any()))
                .thenThrow(new RateNotFoundException("Rate not found"));

        mockMvc.perform(post(MORTGAGE_CHECK_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Rate not found"));
    }
}