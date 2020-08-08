/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.vo.Pagable
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.board.vo.reply.ReplyInfo
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
interface GetRepliesService : ReplyAccessMixin {
    fun getLatestListOf(postId: UUID, clientInfo: ClientAuthInfo?): Pagable<ReplyInfo>
}
