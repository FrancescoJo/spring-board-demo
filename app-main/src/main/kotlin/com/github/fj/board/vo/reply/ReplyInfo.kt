/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.reply

import com.github.fj.board.persistence.entity.reply.Reply
import com.github.fj.board.persistence.model.auth.PlatformType
import java.net.InetAddress
import java.time.LocalDateTime
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
interface ReplyInfo {
    val postId: UUID
    val replyId: UUID
    val writerNickname: String?
    val writerLoginName: String?
    val lastModifiedDate: LocalDateTime
    val lastModifiedIp: InetAddress
    val lastModifiedPlatformType: PlatformType
    val edited: Boolean
    val number: Long
    val contents: String

    companion object {
        private data class Impl(
            override val postId: UUID,
            override val replyId: UUID,
            override val writerNickname: String?,
            override val writerLoginName: String?,
            override val lastModifiedDate: LocalDateTime,
            override val lastModifiedIp: InetAddress,
            override val lastModifiedPlatformType: PlatformType,
            override val edited: Boolean,
            override val number: Long,
            override val contents: String
        ) : ReplyInfo

        fun from(src: Reply): ReplyInfo = Impl(
            postId = src.post.accessId,
            replyId = src.accessId,
            writerNickname = src.creator.nickname,
            writerLoginName = src.creator.authentication.loginName,
            lastModifiedDate = src.lastModifiedDate,
            lastModifiedIp = src.lastModifiedIp,
            lastModifiedPlatformType = src.lastModifiedPlatformType,
            edited = src.edited,
            number = src.number,
            contents = src.contents
        )
    }
}
