/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.reply

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.OkResponseDto
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
 * @since 07 - Aug - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
interface DeleteReplyController {
    @RequestMapping(
        path = [ApiPaths.REPLY_ID],
        method = [RequestMethod.DELETE]
    )
    fun delete(
        @Pattern(
            regexp = REGEX_UUID,
            message = "`replyId` must be in a UUID format."
        )
        @PathVariable
        replyId: String,

        clientInfo: ClientAuthInfo
    ): OkResponseDto<Boolean>
}

@RestController
internal class DeleteReplyControllerImpl(
    private val svc: DeleteReplyService
) : DeleteReplyController {
    override fun delete(replyId: String, clientInfo: ClientAuthInfo): OkResponseDto<Boolean> {
        LOG.debug("{}: {}", clientInfo.requestLine)

        val result = svc.delete(UUID.fromString(replyId), clientInfo)

        return OkResponseDto(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DeleteReplyController::class.java)
    }
}
