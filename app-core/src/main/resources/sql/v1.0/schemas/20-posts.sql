CREATE TABLE IF NOT EXISTS `posts`
(
    `id`                           BIGINT        NOT NULL PRIMARY KEY,
    `access_id`                    VARBINARY(16) NOT NULL UNIQUE,
    `status`                       VARCHAR(4)    NOT NULL DEFAULT '',
    `board_id`                     BIGINT        NOT NULL,
    `user_id`                      BIGINT        NOT NULL,
    `parent_thread`                BIGINT,
    `last_active_date`             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_active_ip`               VARBINARY(16) NOT NULL,
    `last_active_platform_type`    VARCHAR(4)    NOT NULL DEFAULT '',
    `last_active_platform_version` TEXT          NOT NULL DEFAULT '',
    `last_active_app_version`      VARCHAR(32)   NOT NULL DEFAULT '',
    `edited`                       BIT           NOT NULL DEFAULT '1',
    `title`                        VARCHAR(255),
    `contents`                     TEXT,
    `version`                      BIGINT        NOT NULL,

    CONSTRAINT FK_Posts_Board_id FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT FK_Posts_User_id FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS IDX_Posts_Title ON `posts` (`title`);
