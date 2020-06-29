CREATE TABLE IF NOT EXISTS `authentications`
(
    `id`                           BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `login_name`                   VARCHAR(32)    NOT NULL UNIQUE,
    `password`                     VARBINARY(128) NOT NULL,
    `created_date`                 DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_ip`                   VARBINARY(16)  NOT NULL,
    `last_active_date`             DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_active_ip`               VARBINARY(16)  NOT NULL,
    `last_active_platform_type`    VARCHAR(4)     NOT NULL DEFAULT '',
    `last_active_platform_version` VARCHAR(64)    NOT NULL DEFAULT '',
    `last_active_app_version`      VARCHAR(32)    NOT NULL DEFAULT '',
    `version`                      BIGINT         NOT NULL
);
