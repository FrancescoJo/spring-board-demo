/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.OkResponseDto
import com.github.fj.board.service.post.DeletePostService
import com.github.fj.board.service.reply.DeleteReplyService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.text.REGEX_UUID
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.constraints.Pattern

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Aug - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
interface DeletePostController {
    @RequestMapping(
        path = [ApiPaths.POST_ID],
        method = [RequestMethod.DELETE]
    )
    fun delete(
        @Pattern(
            regexp = REGEX_UUID,
            message = "`boardId` must be in a UUID format."
        )
        @PathVariable
        postId: String,

        clientInfo: ClientAuthInfo
    ): OkResponseDto<Boolean>
}

@RestController
internal class DeletePostControllerImpl(
    private val postSvc: DeletePostService,
    private val replySvc: DeleteReplyService
) : DeletePostController {
    override fun delete(postId: String, clientInfo: ClientAuthInfo): OkResponseDto<Boolean> {
        LOG.debug("{}", clientInfo.requestLine)

        val postAccessId = UUID.fromString(postId)
        // Delete replies first asynchronously
        replySvc.deleteAllIn(postAccessId)
        val result = postSvc.delete(postAccessId, clientInfo)

        return OkResponseDto(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DeletePostController::class.java)
    }
}
