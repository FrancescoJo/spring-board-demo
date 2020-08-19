/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.vo.auth.ClientAuthInfo
import org.springframework.scheduling.annotation.Async
import java.util.*
import java.util.concurrent.Future

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 07 - Aug - 2020
 */
interface DeleteReplyService : ReplyEditingServiceMixin {
    fun delete(replyId: UUID, clientInfo: ClientAuthInfo): Boolean

    @Async
    fun deleteAllIn(postId: UUID): Future<Boolean>
}
