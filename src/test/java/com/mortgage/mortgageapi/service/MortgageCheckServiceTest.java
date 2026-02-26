package com.mortgage.mortgageapi.service;

import com.mortgage.mortgageapi.entity.InterestRateEntity;
import com.mortgage.mortgageapi.exception.BusinessRuleViolationException;
import com.mortgage.mortgageapi.exception.RateNotFoundException;
import com.mortgage.mortgageapi.mapper.InterestRateMapper;
import com.mortgage.mortgageapi.model.MortgageCheckRequest;
import com.mortgage.mortgageapi.model.MortgageCheckResponse;
import com.mortgage.mortgageapi.repository.InterestRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MortgageCheckServiceTest {

    @Mock
    private InterestRateRepository repository;
    private MortgageCheckService service;

    @BeforeEach
    void setUp() {
        InterestRateMapper realMapper = new InterestRateMapper();
        AmortizationCalculator realCalculator = new AmortizationCalculator();
        service = new MortgageCheckService(repository, realMapper, realCalculator);
    }

    @Test
    @DisplayName("Should calculate monthly costs correctly for a feasible mortgage")
    void shouldCalculateCorrectCosts() {
        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("60000"), 30, new BigDecimal("200000"), new BigDecimal("250000"));

        InterestRateEntity mockEntity = new InterestRateEntity(30, new BigDecimal("3.500"), LocalDateTime.now());
        when(repository.findByMaturityPeriod(30)).thenReturn(Optional.of(mockEntity));

        MortgageCheckResponse response = service.checkMortgage(request);

        assertThat(response.feasible()).isTrue();
        assertThat(response.monthlyCosts()).isEqualByComparingTo("898.09");
    }

    @Test
    @DisplayName("Should throw BusinessRuleViolationException when loan exceeds 4x income")
    void shouldThrowWhenLoanExceedsIncomeLimit() {
        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("50000"), 30, new BigDecimal("250000"), new BigDecimal("300000"));

        assertThatThrownBy(() -> service.checkMortgage(request))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("exceeds maximum allowed based on income");
    }

    @Test
    @DisplayName("Should throw RateNotFoundException when maturity period is not in DB")
    void shouldThrowExceptionWhenRateMissing() {
        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("80000"), 15, new BigDecimal("100000"), new BigDecimal("150000"));

        when(repository.findByMaturityPeriod(15)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.checkMortgage(request))
                .isInstanceOf(RateNotFoundException.class)
                .hasMessageContaining("period: 15");
    }

    @Test
    @DisplayName("Should throw BusinessRuleViolationException when loan exceeds home value")
    void shouldThrowWhenLoanExceedsHomeValue() {
        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("100000"), 30, new BigDecimal("250000"), new BigDecimal("240000"));

        assertThatThrownBy(() -> service.checkMortgage(request))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("cannot exceed the home value");
    }

    @Test
    @DisplayName("Should handle 0% interest rate correctly (Simple division)")
    void shouldHandleZeroPercentInterestRate() {
        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("60000"), 10, new BigDecimal("120000"), new BigDecimal("200000"));

        InterestRateEntity mockEntity = new InterestRateEntity(10, BigDecimal.ZERO, LocalDateTime.now());
        when(repository.findByMaturityPeriod(10)).thenReturn(Optional.of(mockEntity));

        MortgageCheckResponse response = service.checkMortgage(request);

        assertThat(response.feasible()).isTrue();
        assertThat(response.monthlyCosts()).isEqualByComparingTo("1000.00");
    }
}