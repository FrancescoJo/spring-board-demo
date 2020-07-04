CREATE TABLE IF NOT EXISTS `boards`
(
    `id`            BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `access_id`     VARBINARY(16) NOT NULL UNIQUE,
    `name`          VARCHAR(128)  NOT NULL,
    `posts_count`   BIGINT        NOT NULL DEFAULT 0,
    `created_date`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_date` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `version`       BIGINT        NOT NULL
);

CREATE INDEX IF NOT EXISTS IDX_Boards_Name ON `boards`(`name`);