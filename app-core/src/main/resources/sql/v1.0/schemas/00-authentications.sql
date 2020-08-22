CREATE TABLE IF NOT EXISTS `authentications`
(
    `id`                           BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `status`                       VARCHAR(4)     NOT NULL,
    `login_name`                   VARCHAR(32)    NOT NULL UNIQUE,
    `password`                     VARBINARY(32)  NOT NULL,
    'refresh_token'                BLOB           NOT NULL,
    'refresh_token_issued_at'      DATETIME       NOT NULL,
    'refresh_token_expire_at'      DATETIME       NOT NULL,
    `version`                      BIGINT         NOT NULL
);
