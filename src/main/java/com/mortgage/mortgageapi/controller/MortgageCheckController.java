package com.mortgage.mortgageapi.controller;

import com.mortgage.mortgageapi.model.MortgageCheckRequest;
import com.mortgage.mortgageapi.model.MortgageCheckResponse;
import com.mortgage.mortgageapi.service.MortgageCheckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mortgage.mortgageapi.util.ApiConstants.MORTGAGE_CHECK_ENDPOINT;


@RestController
@RequestMapping(MORTGAGE_CHECK_ENDPOINT)
@RequiredArgsConstructor
public class MortgageCheckController {

    private final MortgageCheckService mortgageCheckService;

    @PostMapping
    public ResponseEntity<MortgageCheckResponse> checkMortgage(@Valid @RequestBody MortgageCheckRequest request) {
        return ResponseEntity.ok(mortgageCheckService.checkMortgage(request));
    }
}
