/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.reply

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.reply.request.CreateReplyRequest
import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.service.reply.CreateReplyService
import com.github.fj.board.vo.auth.ClientAuthInfo
import com.github.fj.lib.text.REGEX_UUID
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Pattern

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
interface CreateReplyController {
    @RequestMapping(
        path = [ApiPaths.POST_ID_REPLY],
        method = [RequestMethod.POST]
    )
    fun create(
        @Pattern(
            regexp = REGEX_UUID,
            message = "`postId` must be in a UUID format."
        )
        @PathVariable
        @Suppress("MVCPathVariableInspection") postId: String,

        @Valid @RequestBody
        req: CreateReplyRequest,

        clientInfo: ClientAuthInfo
    ): ReplyInfoResponse
}

@RestController
internal class CreateReplyControllerImpl(
    private val svc: CreateReplyService
) : CreateReplyController {
    override fun create(postId: String, req: CreateReplyRequest, clientInfo: ClientAuthInfo): ReplyInfoResponse {
        LOG.debug("{}: {}", clientInfo.requestLine, req)

        val result = svc.create(UUID.fromString(postId), req, clientInfo)

        return ReplyInfoResponse.from(result)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CreateReplyController::class.java)
    }
}
