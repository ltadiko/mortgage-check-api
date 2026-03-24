package com.mortgage.mortgageapi.mapper;

import com.mortgage.mortgageapi.entity.InterestRateEntity;
import com.mortgage.mortgageapi.model.InterestRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InterestRateMapperTest {

    private InterestRateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new InterestRateMapper();
    }

    @Test
    @DisplayName("Should map all entity fields correctly to domain record")
    void shouldMapEntityToDomain() {
        // given
        LocalDateTime now = LocalDateTime.now();
        InterestRateEntity entity = new InterestRateEntity();
        entity.setMaturityPeriod(10);
        entity.setInterestRate(new BigDecimal("3.525"));
        entity.setLastUpdate(now);

        // when
        InterestRate result = mapper.toDomain(entity);

        // then
        assertThat(result).isNotNull();
        assertThat(result.maturityPeriod()).isEqualTo(10);
        assertThat(result.interestRate()).isEqualByComparingTo("3.525");
        assertThat(result.lastUpdate()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should return null when entity is null")
    void shouldReturnNullWhenEntityIsNull() {
        // when
        InterestRate result = mapper.toDomain(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle null interest rate field in entity")
    void shouldHandleNullInterestRateField() {
        InterestRateEntity entity = new InterestRateEntity(10, null, null);
        InterestRate result = mapper.toDomain(entity);

        assertThat(result).isNotNull();
        assertThat(result.interestRate()).isNull();
        assertThat(result.lastUpdate()).isNull();
    }
}