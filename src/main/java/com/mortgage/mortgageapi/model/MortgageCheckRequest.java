package com.mortgage.mortgageapi.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MortgageCheckRequest(
        @NotNull @Positive(message = "Income must be greater than 0")
        BigDecimal income,

        @NotNull @Positive(message = "Maturity period must be greater than 0")
        Integer maturityPeriod,

        @NotNull @Positive(message = "Loan value must be greater than 0")
        BigDecimal loanValue,

        @NotNull @Positive(message = "Home value must be greater than 0")
        BigDecimal homeValue
) {
}
