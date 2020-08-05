/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.endpoint.v1.reply.request.CreateReplyRequest
import com.github.fj.board.service.board.BoardAuthorisationMixin
import com.github.fj.board.service.post.PostAccessMixin
import com.github.fj.board.service.user.UserServiceMixin
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.ReplyInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
interface CreateReplyService : UserServiceMixin, BoardAuthorisationMixin, PostAccessMixin, ReplyAccessMixin,
    ReplyServiceMixin {
    fun create(postId: UUID, req: CreateReplyRequest, clientInfo: ClientAuthInfo): ReplyInfo
}
