-- liquibase formatted sql

-- changeset I1nur:1757021837192-1
CREATE SEQUENCE IF NOT EXISTS user_seq START WITH 1 INCREMENT BY 50;

-- changeset I1nur:1757021837192-2
CREATE TABLE session
(
    id       VARCHAR(255) NOT NULL,
    st_time  TIMESTAMP WITHOUT TIME ZONE,
    duration BIGINT,
    user_id  BIGINT,
    CONSTRAINT pk_session PRIMARY KEY (id)
);

-- changeset I1nur:1757021837192-3
CREATE TABLE "user"
(
    id       BIGINT NOT NULL,
    login    VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

-- changeset I1nur:1757021837192-4
ALTER TABLE session
    ADD CONSTRAINT uc_session_user UNIQUE (user_id);

-- changeset I1nur:1757021837192-5
ALTER TABLE session
    ADD CONSTRAINT FK_SESSION_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);

