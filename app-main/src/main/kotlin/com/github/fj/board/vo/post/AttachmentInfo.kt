/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.vo.post

import com.github.fj.board.persistence.entity.post.Attachment
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Jul - 2020
 */
interface AttachmentInfo {
    val accessId: UUID
    val parentPostId: UUID
    val name: String
    val uri: String
    val mimeType: String

    companion object {
        private data class Impl(
            override val accessId: UUID,
            override val parentPostId: UUID,
            override val name: String,
            override val uri: String,
            override val mimeType: String
        ) : AttachmentInfo

        fun from(src: Attachment): AttachmentInfo = Impl(
            accessId = src.accessId,
            parentPostId = src.post.accessId,
            name = src.name,
            uri = src.uri,
            mimeType = src.mimeType
        )
    }
}
