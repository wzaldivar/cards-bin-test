CREATE TABLE country_costs (
    country_code VARCHAR(2) PRIMARY KEY,
    cost NUMERIC
);

CREATE INDEX idx_country_code ON country_costs (country_code);
