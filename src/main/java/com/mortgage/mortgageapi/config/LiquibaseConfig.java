package com.mortgage.mortgageapi.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Value("${spring.liquibase.change-log:classpath:/db/changelog/db.changelog-master.yaml}")
    private String changeLogPath;

    @Value("${spring.liquibase.enabled:true}")
    private boolean shouldRun;

    @Value("${spring.liquibase.contexts:development}")
    private String liquibaseContexts;

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(changeLogPath);
        liquibase.setShouldRun(shouldRun);
        liquibase.setContexts(liquibaseContexts);
        return liquibase;
    }
}
