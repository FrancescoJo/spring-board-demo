/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.reply

import com.github.fj.board.persistence.entity.reply.Reply
import com.github.fj.board.vo.auth.ClientAuthInfo
import java.time.LocalDateTime

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Aug - 2020
 */
interface ReplyServiceMixin {
    fun Reply.applyLastActivityWith(clientInfo: ClientAuthInfo, timestamp: LocalDateTime) {
        this.lastModifiedDate = timestamp
        this.lastModifiedIp = clientInfo.remoteAddr
        this.lastModifiedPlatformType = clientInfo.platformType
    }
}
