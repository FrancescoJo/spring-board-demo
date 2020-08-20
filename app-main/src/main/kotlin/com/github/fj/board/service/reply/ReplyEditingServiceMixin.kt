/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.exception.GeneralHttpException
import com.github.fj.board.persistence.entity.reply.Reply
import com.github.fj.board.persistence.entity.user.User
import com.github.fj.board.service.board.BoardAuthorisationMixin
import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
interface ReplyEditingServiceMixin : UserServiceMixin, ReplyAccessMixin, ReplyServiceMixin, BoardAuthorisationMixin {
    fun checkEditable(
        replyId: UUID,
        clientInfo: ClientAuthInfo,
        onForbiddenException: () -> GeneralHttpException
    ): Pair<User, Reply> {
        val self = clientInfo.getCurrentAccessibleUser()
        val reply = replyId.getReply()
        with(reply) {
            checkIsOwnedBy(self, onForbiddenException)
            post.checkIsReplyEditableFor(self, onForbiddenException)
            post.board.checkIsWritableFor(self, onForbiddenException)
        }

        return self to reply
    }
}
