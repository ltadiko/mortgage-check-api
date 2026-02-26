package com.mortgage.mortgageapi.controller;

import com.mortgage.mortgageapi.model.InterestRate;
import com.mortgage.mortgageapi.service.InterestRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.mortgage.mortgageapi.util.ApiConstants.INTEREST_RATES_ENDPOINT;


@RestController
@RequestMapping(INTEREST_RATES_ENDPOINT)
@RequiredArgsConstructor
public class InterestRateController {
    private final InterestRateService interestRateService;

    @GetMapping
    public List<InterestRate> getRates() {
        return interestRateService.getCurrentRates();
    }
}
