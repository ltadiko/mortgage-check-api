package com.mortgage.mortgageapi.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class AmortizationCalculator {

    private static final BigDecimal MONTHS_PER_YEAR = BigDecimal.valueOf(12);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    // 12 * 100 = 1200 (Used to convert an annual percentage like 4.5% to a monthly decimal like 0.00375)
    private static final BigDecimal ANNUAL_TO_MONTHLY_DECIMAL_DIVISOR = MONTHS_PER_YEAR.multiply(ONE_HUNDRED);

    /**
     * Calculates the monthly mortgage payment using the standard fixed-rate amortization formula:
     * M = P [ r(1 + r)^n ] / [ (1 + r)^n – 1 ]
     *
     * <p>Where:
     * <ul>
     * <li>M = Total monthly payment</li>
     * <li>P = Principal loan amount</li>
     * <li>r = Monthly interest rate (annual rate / 12)</li>
     * <li>n = Total number of payments (years * 12)</li>
     * </ul>
     * * <p><b>Domain Assumption (Fixed-Rate Term):</b> This calculation assumes the interest rate
     * remains fixed for the entire maturity period. In real-world scenarios (e.g., Dutch mortgages),
     * the rate may only be fixed for a subset of the maturity period (rentevastperiode), after which
     * it becomes dynamic. This method calculates the amortized cost as if the current rate applies
     * to the full term.
     *
     * <p>This implementation uses {@link MathContext#DECIMAL128} for intermediate calculations
     * to maintain 34-digit precision, preventing rounding drift during exponentiation.
     *
     * @param principal         The total amount borrowed.
     * @param annualRatePercent The annual interest rate as a percentage (e.g., 3.5).
     * @param years             The mortgage maturity period in years.
     * @return The monthly cost rounded to 2 decimal places using {@link RoundingMode#HALF_UP}.
     */
    public BigDecimal calculateMonthlyCosts(BigDecimal principal, BigDecimal annualRatePercent, int years) {
        if (principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Principal must be greater than 0");
        }
        if (years <= 0) {
            throw new IllegalArgumentException("Years must be greater than 0");
        }

        // 1. Calculate the monthly interest rate decimal (r) using DECIMAL128 to prevent repeating decimal truncation
        BigDecimal monthlyRate = annualRatePercent.divide(ANNUAL_TO_MONTHLY_DECIMAL_DIVISOR, MathContext.DECIMAL128);

        // 2. Calculate total number of payments (n)
        int totalPayments = years * MONTHS_PER_YEAR.intValue();

        // 3. Handle 0% interest edge case
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(totalPayments), 2, RoundingMode.HALF_UP);
        }

        // 4. Calculate the Compounded Interest Factor: (1 + r)^n
        BigDecimal compoundedInterestFactor = BigDecimal.ONE.add(monthlyRate).pow(totalPayments, MathContext.DECIMAL128);

        // 5. Calculate Top of Fraction: P * r * (1 + r)^n
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(compoundedInterestFactor);

        // 6. Calculate Bottom of Fraction: (1 + r)^n - 1
        BigDecimal denominator = compoundedInterestFactor.subtract(BigDecimal.ONE);

        // 7. Final Division and Rounding: Numerator / Denominator
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}