package com.mortgage.mortgageapi.service;

import com.mortgage.mortgageapi.entity.InterestRateEntity;
import com.mortgage.mortgageapi.mapper.InterestRateMapper;
import com.mortgage.mortgageapi.model.InterestRate;
import com.mortgage.mortgageapi.repository.InterestRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterestRateServiceTest {

    @Mock
    private InterestRateRepository repository;

    @Mock
    private InterestRateMapper mapper;

    private InterestRateService service;

    @BeforeEach
    void setUp() {
        service = new InterestRateService(repository, mapper);
    }

    @Test
    @DisplayName("Should return mapped list of current interest rates")
    void shouldReturnMappedInterestRates() {
        // Given: repository returns 2 entities
        LocalDateTime now = LocalDateTime.now();
        InterestRateEntity entity1 = new InterestRateEntity(15, new BigDecimal("3.5"), now);
        InterestRateEntity entity2 = new InterestRateEntity(30, new BigDecimal("4.0"), now);
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        // Mapper returns domain models
        InterestRate rate1 = new InterestRate(15, new BigDecimal("3.5"), now);
        InterestRate rate2 = new InterestRate(30, new BigDecimal("4.0"), now);
        when(mapper.toDomain(entity1)).thenReturn(rate1);
        when(mapper.toDomain(entity2)).thenReturn(rate2);

        // When
        List<InterestRate> result = service.getCurrentRates();

        // Then
        assertThat(result)
                .hasSize(2)
                .containsExactly(rate1, rate2);
    }

    @Test
    @DisplayName("Should return rates sorted by maturity period ascending")
    void shouldReturnRatesSortedByMaturityPeriod() {
        // Given: repository returns entities in DESCENDING order
        LocalDateTime now = LocalDateTime.now();
        InterestRateEntity entity30 = new InterestRateEntity(30, new BigDecimal("4.5"), now);
        InterestRateEntity entity10 = new InterestRateEntity(10, new BigDecimal("3.0"), now);
        InterestRateEntity entity20 = new InterestRateEntity(20, new BigDecimal("3.8"), now);
        when(repository.findAll()).thenReturn(List.of(entity30, entity10, entity20));

        InterestRate rate30 = new InterestRate(30, new BigDecimal("4.5"), now);
        InterestRate rate10 = new InterestRate(10, new BigDecimal("3.0"), now);
        InterestRate rate20 = new InterestRate(20, new BigDecimal("3.8"), now);
        when(mapper.toDomain(entity30)).thenReturn(rate30);
        when(mapper.toDomain(entity10)).thenReturn(rate10);
        when(mapper.toDomain(entity20)).thenReturn(rate20);

        // When
        List<InterestRate> result = service.getCurrentRates();

        // Then: should be sorted ascending by maturity period
        assertThat(result)
                .hasSize(3)
                .containsExactly(rate10, rate20, rate30);
    }

    @Test
    @DisplayName("Should return empty list when no rates are present")
    void shouldReturnEmptyListWhenNoRates() {
        // Given
        when(repository.findAll()).thenReturn(List.of());

        // When
        List<InterestRate> result = service.getCurrentRates();

        // Then
        assertThat(result).isEmpty();
    }
}