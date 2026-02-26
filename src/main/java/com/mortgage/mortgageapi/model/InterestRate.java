package com.mortgage.mortgageapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InterestRate(
        int maturityPeriod,
        BigDecimal interestRate,
        LocalDateTime lastUpdate
) {
}
