CREATE TABLE IF NOT EXISTS `rsa_key_pairs`
(
    `uuid`        VARBINARY(16) NOT NULL PRIMARY KEY,
    `is_enabled`  BIT           NOT NULL DEFAULT '1',
    `private_key` TEXT          NOT NULL,
    `public_key`  TEXT          NOT NULL,
    `issued_at`   DATETIME               DEFAULT CURRENT_TIMESTAMP
);
