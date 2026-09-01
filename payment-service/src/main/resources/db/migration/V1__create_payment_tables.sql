-- ============ PAYMENTS ============
CREATE TABLE payments (
                          id              NUMBER(19,0)       NOT NULL,
                          user_id         NUMBER(19,0)       NOT NULL,
                          from_account_id NUMBER(19,0)       NOT NULL,
                          to_iban         VARCHAR2(34 char)  NOT NULL,
                          to_name         VARCHAR2(100 char) NOT NULL,
                          amount          NUMBER(38,2),
                          currency        VARCHAR2(255 char),
                          status          VARCHAR2(255 char) NOT NULL,
                          description     VARCHAR2(255 char),
                          scheduled_date  TIMESTAMP(9),
                          executed_at     TIMESTAMP(9),
                          created_at      TIMESTAMP(9)       NOT NULL,
                          updated_at      TIMESTAMP(9)       NOT NULL,
                          CONSTRAINT pk_payments PRIMARY KEY (id)
);
CREATE SEQUENCE payments_seq START WITH 1 INCREMENT BY 1;
CREATE INDEX idx_payments_user_id        ON payments (user_id);
CREATE INDEX idx_payments_status         ON payments (status);
CREATE INDEX idx_payments_scheduled_date ON payments (scheduled_date);