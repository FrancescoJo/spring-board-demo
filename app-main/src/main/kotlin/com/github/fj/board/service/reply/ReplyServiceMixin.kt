/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.exception.GeneralHttpException
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.entity.reply.Reply
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.service.post.PostServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Aug - 2020
 */
interface ReplyServiceMixin : PostServiceMixin {
    fun Reply.applyLastActivityWith(clientInfo: ClientAuthInfo, timestamp: LocalDateTime) {
        this.lastModifiedDate = timestamp
        this.lastModifiedIp = clientInfo.remoteAddr
        this.lastModifiedPlatformType = clientInfo.platformType

        this.post.applyLastActivityWith(clientInfo, timestamp)
    }

    fun Reply.checkIsOwnedBy(user: User, onNotOwnedException: () -> GeneralHttpException) {
        if (creator.id != user.id) {
            throw onNotOwnedException.invoke()
        }
    }

    fun Post.checkIsReplyEditableFor(user: User, onForbiddenException: () -> GeneralHttpException) {
        when {
            mode != PostMode.FREE_REPLY     -> throw onForbiddenException.invoke()
            status == ContentStatus.DELETED -> throw onForbiddenException.invoke()
        }
    }
}
