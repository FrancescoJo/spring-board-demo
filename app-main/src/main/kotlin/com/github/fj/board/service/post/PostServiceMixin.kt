/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.service.post

import com.github.fj.board.endpoint.v1.post.request.CreateAttachmentRequest
import com.github.fj.board.persistence.entity.post.Attachment
import com.github.fj.board.persistence.entity.post.Post
import com.github.fj.board.vo.auth.ClientAuthInfo
import java.time.LocalDateTime
import java.util.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
interface PostServiceMixin {
    fun Post.applyLastActivityWith(clientInfo: ClientAuthInfo, timestamp: LocalDateTime) {
        this.lastModifiedDate = timestamp
        this.lastModifiedIp = clientInfo.remoteAddr
        this.lastModifiedPlatformType = clientInfo.platformType
    }

    fun CreateAttachmentRequest.toEntityOf(parentPost: Post) = Attachment().apply {
        val req = this@toEntityOf

        this.accessId = UUID.randomUUID()
        this.post = parentPost
        this.name = req.name
        this.uri = req.uri
        this.mimeType = req.mimeType
    }
}
