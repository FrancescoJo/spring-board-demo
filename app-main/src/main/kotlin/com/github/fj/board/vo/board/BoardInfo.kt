/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.board

import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.vo.user.UserInfo
import java.time.LocalDateTime
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
interface BoardInfo {
    val accessId: UUID
    val key: String
    val name: String
    val description: String
    val postsCount: Long
    val createdDate: LocalDateTime
    val modifiedDate: LocalDateTime
    val creator: UserInfo

    companion object {
        private data class Impl(
            override val accessId: UUID,
            override val key: String,
            override val name: String,
            override val description: String,
            override val postsCount: Long,
            override val createdDate: LocalDateTime,
            override val modifiedDate: LocalDateTime,
            override val creator: UserInfo
        ) : BoardInfo

        fun from(src: Board): BoardInfo = Impl(
            accessId = src.accessId,
            key = src.key,
            name = src.name,
            description = src.description,
            postsCount = src.postsCount,
            createdDate = src.createdDate,
            modifiedDate = src.modifiedDate,
            creator = UserInfo.from(src.creator)
        )
    }
}
