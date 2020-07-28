CREATE TABLE IF NOT EXISTS `post_reactions`
(
    `id`        BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `access_id` VARBINARY(16) NOT NULL UNIQUE,
    `post_id`   BIGINT        NOT NULL,
    `kind`      VARCHAR(4)    NOT NULL DEFAULT '',
    `count`     BIGINT        NOT NULL DEFAULT 0,
    `version`   BIGINT        NOT NULL,

    CONSTRAINT FK_PostReactions_Post_id FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
        ON DELETE CASCADE
);
