package com.mortgage.mortgageapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Response containing mortgage feasibility result and monthly costs")
public record MortgageCheckResponse(
        @Schema(description = "Whether the mortgage is feasible based on business rules", example = "true")
        boolean feasible,

        @Schema(description = "Calculated monthly amortized cost", example = "898.09")
        BigDecimal monthlyCosts,

        @Schema(description = "Descriptive message about the result", example = "Mortgage is feasible.")
        String message
) {
    public static MortgageCheckResponse success(BigDecimal monthlyCosts) {
        return new MortgageCheckResponse(true, monthlyCosts, "Mortgage is feasible.");
    }

    public static MortgageCheckResponse rejected(String reason) {
        return new MortgageCheckResponse(false, BigDecimal.ZERO, reason);
    }
}
