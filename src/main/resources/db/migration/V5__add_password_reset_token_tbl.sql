
create table password_reset_tokens(
id           BIGINT          NOT NULL    AUTO_INCREMENT,
user_id      BIGINT          NOT NULL,
code_hash  VARCHAR(255)      NOT NULL,
expires_at   DATETIME        NOT NULL,
used         BOOLEAN         NOT NULL    DEFAULT FALSE,
created_at   DATETIME        NOT NULL,

CONSTRAINT pk_password_reset_tokens PRIMARY KEY(id),
CONSTRAINT fk_prt_user  FOREIGN KEY (user_id) REFERENCES users(id)
);