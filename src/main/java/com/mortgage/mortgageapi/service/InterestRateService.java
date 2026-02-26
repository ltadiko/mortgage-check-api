package com.mortgage.mortgageapi.service;

import com.mortgage.mortgageapi.model.InterestRate;
import com.mortgage.mortgageapi.mapper.InterestRateMapper;
import com.mortgage.mortgageapi.repository.InterestRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestRateService {

    private final InterestRateRepository interestRateRepository;
    private final InterestRateMapper interestRateMapper;

    public List<InterestRate> getCurrentRates() {
        return interestRateRepository.findAll().stream()
                .map(interestRateMapper::toDomain)
                .toList();
    }
}