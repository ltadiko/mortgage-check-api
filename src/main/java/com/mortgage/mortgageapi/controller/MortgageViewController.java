package com.mortgage.mortgageapi.controller;

import com.mortgage.mortgageapi.exception.BusinessRuleViolationException;
import com.mortgage.mortgageapi.exception.RateNotFoundException;
import com.mortgage.mortgageapi.model.MortgageCheckRequest;
import com.mortgage.mortgageapi.model.MortgageCheckResponse;
import com.mortgage.mortgageapi.service.InterestRateService;
import com.mortgage.mortgageapi.service.MortgageCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class MortgageViewController {

    private final MortgageCheckService mortgageCheckService;
    private final InterestRateService interestRateService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("rates", interestRateService.getCurrentRates());
        return "index";
    }

    @PostMapping("/check")
    public String checkMortgage(
            @RequestParam BigDecimal income,
            @RequestParam Integer maturityPeriod,
            @RequestParam BigDecimal loanValue,
            @RequestParam BigDecimal homeValue,
            Model model) {

        model.addAttribute("rates", interestRateService.getCurrentRates());
        model.addAttribute("income", income);
        model.addAttribute("maturityPeriod", maturityPeriod);
        model.addAttribute("loanValue", loanValue);
        model.addAttribute("homeValue", homeValue);

        try {
            MortgageCheckRequest request = new MortgageCheckRequest(income, maturityPeriod, loanValue, homeValue);
            MortgageCheckResponse response = mortgageCheckService.checkMortgage(request);

            model.addAttribute("result", response);
            model.addAttribute("success", true);
        } catch (BusinessRuleViolationException ex) {
            model.addAttribute("success", false);
            model.addAttribute("errors", ex.getViolations());
        } catch (RateNotFoundException ex) {
            model.addAttribute("success", false);
            model.addAttribute("errors", java.util.List.of(ex.getMessage()));
        }

        return "index";
    }
}
