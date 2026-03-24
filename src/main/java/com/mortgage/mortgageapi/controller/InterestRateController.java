package com.mortgage.mortgageapi.controller;

import com.mortgage.mortgageapi.model.InterestRate;
import com.mortgage.mortgageapi.service.InterestRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.mortgage.mortgageapi.util.ApiConstants.INTEREST_RATES_ENDPOINT;


@RestController
@RequestMapping(INTEREST_RATES_ENDPOINT)
@RequiredArgsConstructor
@Tag(name = "Interest Rates", description = "Retrieve current mortgage interest rates")
public class InterestRateController {
    private final InterestRateService interestRateService;

    @Operation(summary = "Get all current interest rates",
            description = "Returns the list of available mortgage interest rates with their maturity periods and last update timestamps.")
    @ApiResponse(responseCode = "200", description = "List of current interest rates",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = InterestRate.class))))
    @GetMapping
    public ResponseEntity<List<InterestRate>> getRates() {
        return ResponseEntity.ok(interestRateService.getCurrentRates());
    }
}
