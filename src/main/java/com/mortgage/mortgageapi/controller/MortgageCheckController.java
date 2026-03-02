package com.mortgage.mortgageapi.controller;

import com.mortgage.mortgageapi.exception.handler.ApiErrorResponse;
import com.mortgage.mortgageapi.model.MortgageCheckRequest;
import com.mortgage.mortgageapi.model.MortgageCheckResponse;
import com.mortgage.mortgageapi.service.MortgageCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Mortgage Check", description = "Evaluate mortgage feasibility and calculate monthly costs")
public class MortgageCheckController {

    private final MortgageCheckService mortgageCheckService;

    @Operation(summary = "Check mortgage feasibility",
            description = "Validates business rules (loan ≤ 4× income, loan ≤ home value) and calculates the monthly amortized cost for a fixed-rate mortgage.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mortgage feasibility result with monthly costs",
                    content = @Content(schema = @Schema(implementation = MortgageCheckResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error — missing or invalid input fields",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "No interest rate found for the given maturity period",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Business rule violation — loan exceeds allowed limits",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<MortgageCheckResponse> checkMortgage(@Valid @RequestBody MortgageCheckRequest request) {
        return ResponseEntity.ok(mortgageCheckService.checkMortgage(request));
    }
}
