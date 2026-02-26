package com.mortgage.mortgageapi.controller;

import com.mortgage.mortgageapi.model.InterestRate;
import com.mortgage.mortgageapi.service.InterestRateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.mortgage.mortgageapi.util.ApiConstants.INTEREST_RATES_ENDPOINT;

@WebMvcTest(InterestRateController.class)
class InterestRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Use @MockBean if using Spring Boot < 3.4
    private InterestRateService interestRateService;

    @Test
    @DisplayName("Should return list of interest rates with status 200 OK")
    void shouldReturnRates() throws Exception {
        // Given
        LocalDateTime now = LocalDateTime.now();
        List<InterestRate> mockRates = List.of(
                new InterestRate(10, new BigDecimal("3.500"), now),
                new InterestRate(20, new BigDecimal("4.125"), now)
        );

        when(interestRateService.getCurrentRates()).thenReturn(mockRates);

        // when - then
        mockMvc.perform(get(INTEREST_RATES_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                // Verify first element
                .andExpect(jsonPath("$[0].maturityPeriod").value(10))
                .andExpect(jsonPath("$[0].interestRate").value(3.5))
                // Verify second element
                .andExpect(jsonPath("$[1].maturityPeriod").value(20))
                .andExpect(jsonPath("$[1].interestRate").value(4.125));
    }
}