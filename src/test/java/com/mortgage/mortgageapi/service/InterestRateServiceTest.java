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