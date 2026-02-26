package com.mortgage.mortgageapi.repository;

import com.mortgage.mortgageapi.entity.InterestRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestRateRepository extends JpaRepository<InterestRateEntity, Integer> {
    Optional<InterestRateEntity> findByMaturityPeriod(int maturityPeriod);
}