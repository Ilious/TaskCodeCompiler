-- liquibase formatted sql

-- changeset I1nur:1757021837192-1
CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

-- changeset I1nur:1757021837192-2
CREATE TABLE sessions
(
    id       UUID NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id  BIGINT,
    CONSTRAINT pk_session PRIMARY KEY (id)
);

-- changeset I1nur:1757021837192-3
CREATE TABLE "users"
(
    id       BIGINT NOT NULL,
    login    VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

-- changeset I1nur:1757021837192-5
ALTER TABLE sessions
    ADD CONSTRAINT FK_SESSIONS_ON_USERS FOREIGN KEY (user_id) REFERENCES "users" (id);

