CREATE TABLE IF NOT EXISTS `posts`
(
    `id`            BIGINT        NOT NULL PRIMARY KEY,
    `access_id`     VARBINARY(16) NOT NULL UNIQUE,
    `status`        VARCHAR(4)    NOT NULL DEFAULT '',
    `board_id`      BIGINT        NOT NULL,
    `user_id`       BIGINT        NOT NULL,
    `parent_thread` BIGINT,
    `created_date`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `created_ip`    VARBINARY(16) NOT NULL,
    `modified_date` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_ip`   VARBINARY(16) NOT NULL,
    `title`         VARCHAR(255),
    `contents`      TEXT,
    `version`       BIGINT        NOT NULL,

    CONSTRAINT FK_Posts_Board_id FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT FK_Posts_User_id FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS IDX_Posts_Title ON `posts` (`title`);
