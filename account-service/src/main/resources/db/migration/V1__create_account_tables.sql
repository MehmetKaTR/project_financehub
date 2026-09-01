-- ============ CATEGORIES ============
CREATE TABLE categories (
                            id          NUMBER(19,0)       NOT NULL,
                            name        VARCHAR2(50 char)  NOT NULL,
                            type        VARCHAR2(255 char) NOT NULL,
                            icon        VARCHAR2(50 char),
                            color       VARCHAR2(7 char),
                            is_default  BOOLEAN,
                            created_at  TIMESTAMP(9)       NOT NULL,
                            CONSTRAINT pk_categories PRIMARY KEY (id)
);
CREATE SEQUENCE categories_seq START WITH 1 INCREMENT BY 1;
CREATE INDEX idx_categories_type ON categories (type);

-- ============ BANK_ACCOUNTS ============
CREATE TABLE bank_accounts (
                               id                  NUMBER(19,0)       NOT NULL,
                               user_id             NUMBER(19,0)       NOT NULL,
                               bank_name           VARCHAR2(100 char) NOT NULL,
                               bank_account_number VARCHAR2(20 char),
                               iban                VARCHAR2(34 char),
                               amount              NUMBER(38,2),
                               currency            VARCHAR2(255 char),
                               account_type        VARCHAR2(255 char) NOT NULL,
                               is_active           BOOLEAN,
                               created_at          TIMESTAMP(9)       NOT NULL,
                               updated_at          TIMESTAMP(9)       NOT NULL,
                               CONSTRAINT pk_bank_accounts PRIMARY KEY (id)
);
CREATE SEQUENCE bank_accounts_seq START WITH 1 INCREMENT BY 1;
CREATE INDEX idx_bank_accounts_user_id ON bank_accounts (user_id);

-- ============ TRANSACTIONS ============
CREATE TABLE transactions (
                              id               NUMBER(19,0)       NOT NULL,
                              bank_account_id  NUMBER(19,0)       NOT NULL,
                              category_id      NUMBER(19,0),
                              amount           NUMBER(38,2),
                              currency         VARCHAR2(255 char),
                              transaction_type VARCHAR2(255 char) NOT NULL,
                              description      VARCHAR2(255 char),
                              reference_number VARCHAR2(100 char),
                              created_at       TIMESTAMP(9)       NOT NULL,
                              CONSTRAINT pk_transactions PRIMARY KEY (id),
                              CONSTRAINT fk_tx_account  FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id),
                              CONSTRAINT fk_tx_category FOREIGN KEY (category_id)     REFERENCES categories (id)
);
CREATE SEQUENCE transactions_seq START WITH 1 INCREMENT BY 1;
CREATE INDEX idx_transactions_bank_account_id ON transactions (bank_account_id);
CREATE INDEX idx_transactions_category_id     ON transactions (category_id);
CREATE INDEX idx_transactions_created_at      ON transactions (created_at);

-- ============ OUTBOX_EVENTS ============
CREATE TABLE outbox_events (
                               id             NUMBER(19,0)       NOT NULL,
                               aggregate_type VARCHAR2(255 char) NOT NULL,
                               aggregate_id   NUMBER(19,0)       NOT NULL,
                               event_type     VARCHAR2(255 char) NOT NULL,
                               processed      BOOLEAN            NOT NULL,
                               created_at     TIMESTAMP(9),
                               CONSTRAINT pk_outbox_events PRIMARY KEY (id)
);
CREATE SEQUENCE outbox_event_seq START WITH 1 INCREMENT BY 1;
CREATE INDEX idx_outbox_processed ON outbox_events (processed);