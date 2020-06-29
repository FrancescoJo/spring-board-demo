CREATE TABLE IF NOT EXISTS `users`
(
    `id`                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `authentication_id` BIGINT       NOT NULL,
    `nickname`          VARCHAR(64)  NOT NULL UNIQUE,
    `status`            VARCHAR(4)   NOT NULL DEFAULT '',
    `email`             VARCHAR(128) NOT NULL DEFAULT '',
    `invited_by`        BIGINT       NOT NULL DEFAULT 0,
    `gender`            VARCHAR(4)   NOT NULL DEFAULT '',
    `version`           BIGINT       NOT NULL,

    CONSTRAINT FK_Users_Authentication_id FOREIGN KEY (`authentication_id`) REFERENCES `authentications` (`id`)
);
