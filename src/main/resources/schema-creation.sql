CREATE TABLE users (
    id          BIGSERIAL       PRIMARY KEY,
    nickname    VARCHAR(40)     NOT NULL    UNIQUE
);


CREATE TABLE password (
    id          BIGSERIAL       PRIMARY KEY,
    value       VARCHAR(40)     NOT NULL,
    user_id     BIGINT          NOT NULL    UNIQUE      REFERENCES users (id) ON DELETE CASCADE
);


CREATE TABLE time_between_presses (
    id              BIGSERIAL          PRIMARY KEY,
    gap_number      INT                NOT NULL,
    time            INT                NOT NULL,
    password_id     BIGINT             NOT NULL         REFERENCES password (id) ON DELETE CASCADE
);

CREATE TABLE key_press_time (
    id              BIGSERIAL          PRIMARY KEY,
    gap_number      INT                NOT NULL,
    time            INT                NOT NULL,
    password_id     BIGINT             NOT NULL         REFERENCES password (id) ON DELETE CASCADE
);

CREATE TABLE tbp_standard (
    id              BIGSERIAL          PRIMARY KEY,
    gap_number      INT                NOT NULL,
    min             BIGINT,
    max             BIGINT,
    password_id     BIGINT             NOT NULL         REFERENCES password (id) ON DELETE CASCADE
);

CREATE TABLE kpt_standard (
    id              BIGSERIAL          PRIMARY KEY,
    gap_number      INT                NOT NULL,
    min             BIGINT,
    max             BIGINT,
    password_id     BIGINT             NOT NULL         REFERENCES password (id) ON DELETE CASCADE
);