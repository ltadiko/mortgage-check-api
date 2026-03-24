package com.mortgage.mortgageapi;

import com.mortgage.mortgageapi.mapper.InterestRateMapper;
import com.mortgage.mortgageapi.service.AmortizationCalculator;
import com.mortgage.mortgageapi.service.InterestRateService;
import com.mortgage.mortgageapi.service.MortgageCheckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MortgageapplicationApplicationTests {

    @Autowired
    private MortgageCheckService mortgageCheckService;
    @Autowired
    private InterestRateService interestRateService;
    @Autowired
    private AmortizationCalculator amortizationCalculator;
    @Autowired
    private InterestRateMapper interestRateMapper;

    @Test
    void contextLoads() {
        assertThat(mortgageCheckService).isNotNull();
        assertThat(interestRateService).isNotNull();
        assertThat(amortizationCalculator).isNotNull();
        assertThat(interestRateMapper).isNotNull();
    }
}
