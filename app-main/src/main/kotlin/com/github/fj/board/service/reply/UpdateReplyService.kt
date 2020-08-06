/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.endpoint.v1.reply.request.UpdateReplyRequest
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.ReplyInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
interface UpdateReplyService : ReplyEditingServiceMixin {
    fun update(replyId: UUID, req: UpdateReplyRequest, clientInfo: ClientAuthInfo): ReplyInfo
}
