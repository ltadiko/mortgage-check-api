package com.mortgage.mortgageapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "interest_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestRateEntity {

    @Id
    private Integer maturityPeriod;

    @Column(precision = 6, scale = 3)
    private BigDecimal interestRate;

    private LocalDateTime lastUpdate;
}