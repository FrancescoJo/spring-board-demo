CREATE TABLE IF NOT EXISTS `users`
(
    `id`                           BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `authentication_id`            BIGINT        NOT NULL,
    `nickname`                     VARCHAR(64)   NOT NULL UNIQUE,
    `status`                       VARCHAR(4)    NOT NULL DEFAULT '',
    `email`                        VARCHAR(128)  NOT NULL DEFAULT '',
    `created_date`                 DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_ip`                   VARBINARY(16) NOT NULL,
    `last_active_date`             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_active_ip`               VARBINARY(16) NOT NULL,
    `last_active_platform_type`    VARCHAR(4)    NOT NULL DEFAULT '',
    `last_active_platform_version` TEXT          NOT NULL DEFAULT '',
    `last_active_app_version`      VARCHAR(32)   NOT NULL DEFAULT '',
    `invited_by`                   BIGINT,
    `version`                      BIGINT        NOT NULL,

    CONSTRAINT FK_Users_Authentication_id FOREIGN KEY (`authentication_id`) REFERENCES `authentications` (`id`)
);
