package com.mortgage.mortgageapi.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BusinessRuleViolationException extends RuntimeException {

    private final List<String> violations;

    public BusinessRuleViolationException(List<String> violations) {
        super("Mortgage request failed business rules: " + String.join(" | ", violations));
        this.violations = List.copyOf(violations);
    }
}