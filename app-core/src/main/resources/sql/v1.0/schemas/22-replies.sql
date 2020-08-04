CREATE TABLE IF NOT EXISTS `replies`
(
    `id`                          BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `access_id`                   VARBINARY(16) NOT NULL UNIQUE,
    `status`                      VARCHAR(4)    NOT NULL DEFAULT '',
    `post_id`                     BIGINT        NOT NULL,
    `user_id`                     BIGINT        NOT NULL,
    `last_modified_date`          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_modified_ip`            VARBINARY(16) NOT NULL,
    `last_modified_platform_type` VARCHAR(4)    NOT NULL DEFAULT '',
    `edited`                      BIT           NOT NULL DEFAULT '1',
    `number`                      BIGINT        NOT NULL,
    `contents`                    TEXT,
    `version`                     BIGINT        NOT NULL,

    CONSTRAINT FK_Replies_Post_id FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT FK_Replies_User_id FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE
)
