/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.post

import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.persistence.model.post.ContentStatus
import com.github.fj.board.persistence.model.post.PostMode
import java.time.LocalDateTime
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Jul - 2020
 */
interface PostBriefInfo {
    val id: Long
    val accessId: UUID
    val status: ContentStatus
    val mode: PostMode
    val boardId: UUID
    val writerNickname: String?
    val writerLoginName: String?
    val lastModifiedDate: LocalDateTime
    val number: Long
    val title: String
    val viewedCount: Long
    val replyCount: Long

    companion object {
        private data class Impl(
            override val id: Long,
            override val accessId: UUID,
            override val status: ContentStatus,
            override val mode: PostMode,
            override val boardId: UUID,
            override val writerNickname: String?,
            override val writerLoginName: String?,
            override val lastModifiedDate: LocalDateTime,
            override val number: Long,
            override val title: String,
            override val viewedCount: Long,
            override val replyCount: Long
        ) : PostBriefInfo

        fun from(src: Post, replyCount: Long): PostBriefInfo = Impl(
            id = src.id,
            accessId = src.accessId,
            status = src.status,
            mode = src.mode,
            boardId = src.board.accessId,
            writerNickname = src.creator.nickname,
            writerLoginName = src.creator.authentication.loginName,
            lastModifiedDate = src.lastModifiedDate,
            number = src.number,
            title = src.title,
            viewedCount = src.viewedCount,
            replyCount = replyCount
        )
    }
}
