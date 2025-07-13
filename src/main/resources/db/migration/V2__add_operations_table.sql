CREATE TABLE IF NOT EXISTS operations (
    id BIGSERIAL PRIMARY KEY,
    operation VARCHAR(20) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    date TIMESTAMP NOT NULL,
    account_id BIGINT NOT NULL,
    CONSTRAINT fk_operations_account FOREIGN KEY (account_id) REFERENCES account(id)
);