CREATE TABLE IF NOT EXISTS `attachments`
(
    `id`        BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `access_id` VARBINARY(16) NOT NULL UNIQUE,
    `status`    VARCHAR(4)    NOT NULL DEFAULT '',
    `post_id`   BIGINT        NOT NULL,
    `name`      TEXT,
    `uri`       TEXT,
    `mime_type` TEXT,

    CONSTRAINT FK_Attachments_Post_id FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
        ON DELETE CASCADE
);
