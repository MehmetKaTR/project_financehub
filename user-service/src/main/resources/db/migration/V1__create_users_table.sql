-- users tablosu (User entity'sinin karsiligi)
CREATE TABLE users (
                       id            NUMBER(19,0)       NOT NULL,
                       email         VARCHAR2(100 char) NOT NULL,
                       password_hash VARCHAR2(255 char) NOT NULL,
                       full_name     VARCHAR2(255 char) NOT NULL,
                       phone         VARCHAR2(255 char),
                       is_active     BOOLEAN            NOT NULL,
                       created_at    TIMESTAMP(6)       NOT NULL,
                       updated_at    TIMESTAMP(6)       NOT NULL,
                       CONSTRAINT pk_users PRIMARY KEY (id),
                       CONSTRAINT uk_users_email UNIQUE (email)
);

-- id uretimi icin sequence (entity'de GenerationType.SEQUENCE, users_seq)
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;

-- entity'deki @Index'ler
-- CREATE INDEX idx_users_email ON users (email); CONSTRAINT yaptık diye otomatik olusuo index email icin
CREATE INDEX idx_users_phone ON users (phone);