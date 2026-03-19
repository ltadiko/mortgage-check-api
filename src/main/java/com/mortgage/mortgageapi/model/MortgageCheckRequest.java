package com.mortgage.mortgageapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Request payload for mortgage feasibility check")
public record MortgageCheckRequest(
        @NotNull @Positive(message = "Income must be greater than 0")
        @Schema(description = "Annual gross income of the applicant", example = "60000")
        BigDecimal income,

        @NotNull @Positive(message = "Maturity period must be greater than 0")
        @Schema(description = "Mortgage maturity period in years", example = "30")
        Integer maturityPeriod,

        @NotNull @Positive(message = "Loan value must be greater than 0")
        @Schema(description = "Requested loan amount", example = "200000")
        BigDecimal loanValue,

        @NotNull @Positive(message = "Home value must be greater than 0")
        @Schema(description = "Market value of the property", example = "250000")
        BigDecimal homeValue
) {
}
