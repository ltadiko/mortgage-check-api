package com.mortgage.mortgageapi.service;

import com.mortgage.mortgageapi.exception.BusinessRuleViolationException;
import com.mortgage.mortgageapi.exception.RateNotFoundException;
import com.mortgage.mortgageapi.mapper.InterestRateMapper;
import com.mortgage.mortgageapi.model.MortgageCheckRequest;
import com.mortgage.mortgageapi.model.MortgageCheckResponse;
import com.mortgage.mortgageapi.repository.InterestRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MortgageCheckService {

    private final InterestRateRepository repository;
    private final InterestRateMapper mapper;
    private final AmortizationCalculator amortizationCalculator;

    private static final BigDecimal MAX_INCOME_MULTIPLIER = BigDecimal.valueOf(4);

    public MortgageCheckResponse checkMortgage(MortgageCheckRequest request) {

        log.info(
                "Evaluating mortgage request: income={}, loanValue={}, homeValue={}, maturity={}",
                request.income(),
                request.loanValue(),
                request.homeValue(),
                request.maturityPeriod()
        );

        // 1️⃣ Validate business rules (throws if invalid)
        validateLoanConstraints(request);

        // 2️⃣ Retrieve interest rate
        var rate = repository.findByMaturityPeriod(request.maturityPeriod())
                .map(mapper::toDomain)
                .orElseThrow(() -> {
                    log.error("No interest rate found for maturity period={}", request.maturityPeriod());
                    return new RateNotFoundException(
                            "No interest rate found for period: " + request.maturityPeriod());
                });

        // 3️⃣ Calculate monthly costs
        BigDecimal monthlyCosts = amortizationCalculator.calculateMonthlyCosts(
                request.loanValue(),
                rate.interestRate(),
                request.maturityPeriod()
        );

        log.info("Mortgage feasible. Monthly costs calculated: {}", monthlyCosts);

        // 4️⃣ Build response
        return MortgageCheckResponse.success(monthlyCosts);
    }

    private void validateLoanConstraints(MortgageCheckRequest request) {

        List<String> violations = new ArrayList<>();

        BigDecimal maxLoanAllowedByIncome =
                request.income().multiply(MAX_INCOME_MULTIPLIER);

        // Rule 1: Loan cannot exceed 4x income
        if (request.loanValue().compareTo(maxLoanAllowedByIncome) > 0) {
            violations.add(String.format(
                    "Loan exceeds maximum allowed based on income (Max allowed: %s)",
                    maxLoanAllowedByIncome
            ));
        }

        // Rule 2: Loan cannot exceed home value
        if (request.loanValue().compareTo(request.homeValue()) > 0) {
            violations.add(String.format(
                    "Loan cannot exceed the home value (Home value: %s)",
                    request.homeValue()
            ));
        }

        if (!violations.isEmpty()) {
            log.warn("Mortgage request rejected. Violations: {}", violations);
            throw new BusinessRuleViolationException(List.copyOf(violations));
        }
    }
}