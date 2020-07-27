/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.post

import com.github.fj.board.persistence.entity.post.Attachment
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.auth.PlatformType
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.persistence.model.post.PostStatus
import java.net.InetAddress
import java.time.LocalDateTime
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Jul - 2020
 */
interface PostDetailedInfo : PostBriefInfo {
    override val accessId: UUID
    override val status: PostStatus
    override val mode: PostMode
    override val boardId: UUID
    override val writerNickname: String
    override val writerLoginName: String
    override val lastModifiedDate: LocalDateTime
    override val number: Long
    override val title: String
    override val viewedCount: Long
    val lastModifiedIp: InetAddress
    val lastModifiedPlatformType: PlatformType
    val edited: Boolean
    val contents: String
    val attachments: List<AttachmentInfo>

    companion object {
        private data class Impl(
            override val accessId: UUID,
            override val status: PostStatus,
            override val mode: PostMode,
            override val boardId: UUID,
            override val writerNickname: String,
            override val writerLoginName: String,
            override val lastModifiedDate: LocalDateTime,
            override val number: Long,
            override val title: String,
            override val viewedCount: Long,
            override val lastModifiedIp: InetAddress,
            override val lastModifiedPlatformType: PlatformType,
            override val edited: Boolean,
            override val contents: String,
            override val attachments: List<AttachmentInfo>
        ) : PostDetailedInfo

        fun from(src: Post, attachments: List<Attachment>): PostDetailedInfo = Impl(
            accessId = src.accessId,
            status = src.status,
            mode = src.mode,
            boardId = src.board.accessId,
            writerNickname = src.user.nickname,
            writerLoginName = src.user.authentication.loginName,
            lastModifiedDate = src.lastModifiedDate,
            number = src.number,
            title = src.title,
            viewedCount = src.viewedCount,
            lastModifiedIp = src.lastModifiedIp,
            lastModifiedPlatformType = src.lastModifiedPlatformType,
            edited = src.edited,
            contents = src.contents,
            attachments = attachments.map { AttachmentInfo.from(it) }
        )
    }
}
