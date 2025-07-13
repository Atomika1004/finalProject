ALTER TABLE operations
    ADD COLUMN IF NOT EXISTS account_id_transfer BIGINT;

ALTER TABLE operations
    ADD CONSTRAINT fk_operations_account_transfer
        FOREIGN KEY (account_id_transfer) REFERENCES account(id);