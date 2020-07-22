/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.board

import com.github.fj.board.persistence.entity.board.Board
import com.github.fj.board.persistence.model.board.Access
import com.github.fj.board.persistence.model.board.Mode
import com.github.fj.board.persistence.model.board.Status
import com.github.fj.board.vo.user.UserInfo
import java.time.LocalDateTime
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
interface BoardInfo {
    val id: Long
    val accessId: UUID
    val status: Status
    val access: Access
    val mode: Mode
    val key: String
    val name: String
    val description: String
    val postsCount: Long
    val createdDate: LocalDateTime
    val modifiedDate: LocalDateTime
    val creator: UserInfo

    companion object {
        private data class Impl(
            override val id: Long,
            override val accessId: UUID,
            override val status: Status,
            override val access: Access,
            override val mode: Mode,
            override val key: String,
            override val name: String,
            override val description: String,
            override val postsCount: Long,
            override val createdDate: LocalDateTime,
            override val modifiedDate: LocalDateTime,
            override val creator: UserInfo
        ) : BoardInfo

        fun from(src: Board): BoardInfo = Impl(
            id = src.id,
            accessId = src.accessId,
            status = src.status,
            access = src.access,
            mode = src.mode,
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
