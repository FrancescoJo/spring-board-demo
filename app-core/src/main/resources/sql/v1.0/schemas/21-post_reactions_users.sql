CREATE TABLE IF NOT EXISTS `post_reactions_users`
(
    `id`          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `reaction_id` BIGINT NOT NULL,
    `user_id`     BIGINT NOT NULL,

    CONSTRAINT FK_PostReactionUsers_Reaction_id FOREIGN KEY (`reaction_id`) REFERENCES `post_reactions` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT FK_PostReactionUsers_User_id FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE
);
