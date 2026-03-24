-- liquibase formatted sql

-- changeset Lakshmaiah Tatikonda:1 context:production,development
CREATE TABLE interest_rates (
maturity_period INT PRIMARY KEY,
interest_rate DECIMAL(5, 2) NOT NULL,
last_update TIMESTAMP NOT NULL
);

-- changeset Lakshmaiah Tatikonda:2 context:production,development
INSERT INTO interest_rates (maturity_period, interest_rate, last_update) VALUES (5, 3.00, CURRENT_TIMESTAMP);
INSERT INTO interest_rates (maturity_period, interest_rate, last_update) VALUES (10, 3.50, CURRENT_TIMESTAMP);
INSERT INTO interest_rates (maturity_period, interest_rate, last_update) VALUES (15, 3.75, CURRENT_TIMESTAMP);
INSERT INTO interest_rates (maturity_period, interest_rate, last_update) VALUES (20, 4.00, CURRENT_TIMESTAMP);
INSERT INTO interest_rates (maturity_period, interest_rate, last_update) VALUES (25, 4.25, CURRENT_TIMESTAMP);
INSERT INTO interest_rates (maturity_period, interest_rate, last_update) VALUES (30, 4.50, CURRENT_TIMESTAMP);