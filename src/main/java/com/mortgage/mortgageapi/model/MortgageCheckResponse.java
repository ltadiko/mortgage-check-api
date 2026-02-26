package com.mortgage.mortgageapi.model;

import java.math.BigDecimal;

public record MortgageCheckResponse(
        boolean feasible,
        BigDecimal monthlyCosts,
        String message
) {
    public static MortgageCheckResponse success(BigDecimal monthlyCosts) {
        return new MortgageCheckResponse(true, monthlyCosts, "Mortgage is feasible.");
    }

    public static MortgageCheckResponse rejected(String reason) {
        return new MortgageCheckResponse(false, BigDecimal.ZERO, reason);
    }
}
