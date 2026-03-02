package com.mortgage.mortgageapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Current mortgage interest rate for a specific maturity period")
public record InterestRate(
        @Schema(description = "Mortgage maturity period in years", example = "30")
        int maturityPeriod,

        @Schema(description = "Annual interest rate as a percentage", example = "4.50")
        BigDecimal interestRate,

        @Schema(description = "Timestamp of the last rate update")
        LocalDateTime lastUpdate
) {
}
