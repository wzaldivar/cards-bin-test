create table iin_country_mapping (
    iin VARCHAR(6) PRIMARY KEY,
    country_code VARCHAR(2)
);

CREATE INDEX idx_iin ON iin_country_mapping (iin);
