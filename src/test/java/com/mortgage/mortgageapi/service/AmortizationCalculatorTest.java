package com.mortgage.mortgageapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AmortizationCalculatorTest {

    private AmortizationCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new AmortizationCalculator();
    }

    @Test
    @DisplayName("Should correctly calculate a standard 30-year mortgage")
    void shouldCalculateStandardThirtyYearMortgage() {
        // Given
        BigDecimal principal = new BigDecimal("200000");
        BigDecimal rate = new BigDecimal("3.500");
        int years = 30;

        // When
        BigDecimal monthlyCost = calculator.calculateMonthlyCosts(principal, rate, years);

        // Then: $200,000 at 3.5% for 30 years is exactly $898.09
        assertThat(monthlyCost).isEqualByComparingTo("898.09");
    }

    @Test
    @DisplayName("Should correctly calculate a standard 15-year mortgage")
    void shouldCalculateStandardFifteenYearMortgage() {
        // Given
        BigDecimal principal = new BigDecimal("150000");
        BigDecimal rate = new BigDecimal("4.125");
        int years = 15;

        // When
        BigDecimal monthlyCost = calculator.calculateMonthlyCosts(principal, rate, years);
        System.out.println("DEBUG -> Calculated Cost: " + monthlyCost);

        // Then: $150,000 at 4.125% for 15 years is $1118.95
        assertThat(monthlyCost).isEqualByComparingTo("1118.95");
    }

    @Test
    @DisplayName("Should handle 0% interest rate without throwing division by zero")
    void shouldHandleZeroPercentInterestRate() {
        // Given
        BigDecimal principal = new BigDecimal("120000");
        BigDecimal rate = BigDecimal.ZERO;
        int years = 10;

        // When
        BigDecimal monthlyCost = calculator.calculateMonthlyCosts(principal, rate, years);

        // Then: $120,000 / 120 months = $1000.00 exactly
        assertThat(monthlyCost).isEqualByComparingTo("1000.00");
    }

    @Test
    @DisplayName("Should throw when principal is negative")
    void shouldThrowForNegativePrincipal() {
        BigDecimal principal = new BigDecimal("-1000");
        BigDecimal rate = BigDecimal.TEN;
        int years = 10;
        // Isolate the call expected to throw, avoid chaining
        assertThatThrownBy(() -> calculator.calculateMonthlyCosts(principal, rate, years))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw when years is zero")
    void shouldThrowForZeroYears() {
        BigDecimal principal = BigDecimal.valueOf(1000);
        BigDecimal rate = BigDecimal.TEN;
        int years = 0;
        // Isolate the call expected to throw, avoid chaining
        assertThatThrownBy(() -> calculator.calculateMonthlyCosts(principal, rate, years))
                .isInstanceOf(IllegalArgumentException.class);
    }
}