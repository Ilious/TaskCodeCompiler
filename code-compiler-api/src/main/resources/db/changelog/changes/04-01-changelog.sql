-- liquibase formatted sql

-- changeset I1nur:1791120955722-1
CREATE TABLE code_results
(
    id          UUID NOT NULL,
    code_result TEXT NOT NULL,
    code_error  TEXT NOT NULL,
    status      SMALLINT,
    exit_code   BIGINT,
    CONSTRAINT pk_code_results PRIMARY KEY (id)
);

-- changeset I1nur:1791120955722-2
CREATE TABLE tasks
(
    id             UUID     NOT NULL,
    code           TEXT     NOT NULL,
    compiler       SMALLINT NOT NULL,
    status         SMALLINT NOT NULL,
    user_id        BIGINT   NOT NULL,
    code_result_id UUID,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

-- changeset I1nur:1791120955722-3
ALTER TABLE tasks
    ADD CONSTRAINT uc_tasks_code_result UNIQUE (code_result_id);

-- changeset I1nur:1791120955722-4
ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_CODE_RESULT
        FOREIGN KEY (code_result_id) REFERENCES code_results (id);

-- changeset I1nur:1791120955722-5
ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_USER
        FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset I1nur:1791120955722-6
ALTER TABLE users
    ALTER COLUMN login SET NOT NULL;

-- changeset I1nur:1791120955722-7
ALTER TABLE users
    ALTER COLUMN password SET NOT NULL;

-- changeset I1nur:1791120955722-8
ALTER TABLE users
    ADD CONSTRAINT uc_users_login UNIQUE (login);

-- changeset I1nur:1791120955722-9
ALTER TABLE sessions
    ALTER COLUMN user_id SET NOT NULL;