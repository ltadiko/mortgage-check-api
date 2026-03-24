package com.mortgage.mortgageapi.util;

/**
 * Centralized API endpoint constants to ensure consistency between
 * Controllers, Security configurations, and Integration Tests.
 */
public final class ApiConstants {
    private ApiConstants() {
        // Private constructor to prevent instantiation
    }

    public static final String INTEREST_RATES_ENDPOINT = "/api/interest-rates";
    public static final String MORTGAGE_CHECK_ENDPOINT = "/api/mortgage-check";
}