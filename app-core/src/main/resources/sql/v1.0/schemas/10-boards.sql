CREATE TABLE IF NOT EXISTS `boards`
(
    `id`              BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `access_id`       VARBINARY(16) NOT NULL UNIQUE,
    `name`            VARCHAR(128)  NOT NULL,
    `description`     TEXT          NOT NULL,
    `posts_count`     BIGINT        NOT NULL DEFAULT 0,
    `created_date`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_date`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_user_id` BIGINT        NOT NULL,
    `version`         BIGINT        NOT NULL,

    CONSTRAINT FK_Boards_Creator_User_id FOREIGN KEY (`creator_user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS IDX_Boards_Name ON `boards` (`name`);
