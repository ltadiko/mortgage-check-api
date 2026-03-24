package com.mortgage.mortgageapi.mapper;

import com.mortgage.mortgageapi.entity.InterestRateEntity;
import com.mortgage.mortgageapi.model.InterestRate;
import org.springframework.stereotype.Component;

@Component
public class InterestRateMapper {

    /**
     * Converts a database entity into the pure domain record.
     * * @param entity The JPA entity retrieved from the database.
     *
     * @return The immutable domain record.
     */
    public InterestRate toDomain(InterestRateEntity entity) {
        if (entity == null) {
            return null;
        }

        return new InterestRate(
                entity.getMaturityPeriod(), // Auto-unboxed from Integer to int
                entity.getInterestRate(),
                entity.getLastUpdate()
        );
    }
}