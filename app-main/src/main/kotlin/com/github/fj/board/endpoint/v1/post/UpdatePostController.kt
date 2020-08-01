/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.post.request.UpdatePostRequest
import com.github.fj.board.endpoint.v1.post.response.PostInfoBriefResponse
import com.github.fj.board.service.post.UpdatePostService
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
import javax.validation.Valid
import javax.validation.constraints.Pattern

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
@Validated
interface UpdatePostController {
    @RequestMapping(
        path = [ApiPaths.BOARD_ID_POST],
        method = [RequestMethod.PATCH]
    )
    fun create(
        @Pattern(
            regexp = REGEX_UUID,
            message = "`boardId` must be in a UUID format."
        )
        @PathVariable
        @Suppress("MVCPathVariableInspection") boardId: String,
        @Valid @RequestBody req: UpdatePostRequest, clientInfo: ClientAuthInfo
    ): PostInfoBriefResponse
}

@RestController
internal class UpdatePostControllerImpl(
    private val svc: UpdatePostService
) : UpdatePostController {
    override fun create(boardId: String, req: UpdatePostRequest, clientInfo: ClientAuthInfo): PostInfoBriefResponse {
        LOG.debug("{}: {}", clientInfo.requestLine, req)

        TODO("Not yet implemented")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(UpdatePostController::class.java)
    }
}