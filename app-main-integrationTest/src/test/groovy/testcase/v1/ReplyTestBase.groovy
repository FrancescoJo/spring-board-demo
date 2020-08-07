/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1

import com.github.fj.board.endpoint.v1.reply.request.CreateReplyRequest
import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.vo.board.BoardInfo
import com.github.fj.board.vo.post.PostBriefInfo
import com.github.fj.board.vo.post.PostDetailedInfo
import com.github.fj.board.vo.reply.ReplyInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.transaction.support.TransactionTemplate
import test.com.github.fj.board.endpoint.ApiPathsHelper
import test.com.github.fj.board.endpoint.v1.reply.dto.CreateReplyRequestBuilder
import testcase.common.CreatedUser

import javax.annotation.Nonnull

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
class ReplyTestBase extends PostTestBase {
    @Autowired
    private ReplyRepository replyRepo

    @Autowired
    private TransactionTemplate txTemplate

    protected CreatedUser self
    protected BoardInfo currentBoard
    protected PostDetailedInfo currentPost

    def setup() {
        this.self = createRandomUser()
        this.currentBoard = createRandomBoardOf(self)
        this.currentPost = createRandomPostOf(self, currentBoard)
    }

    def cleanup() {
        replyRepo.deleteAll()
    }

    protected final void updateCurrentBoardState(final @Nonnull BoardStatus status, final @Nonnull BoardMode mode) {
        if (mode != null) {
            updateBoardMode(self, currentBoard, mode)
        }

        if (status != null) {
            updateBoardStatus(currentBoard.accessId, status)
        }
    }

    protected final void updateCurrentPostMode(final PostMode mode) {
        updatePostMode(self, currentPost, mode)
    }

    protected final ReplyInfo createRandomReplyOf(final CreatedUser owner, final PostBriefInfo post) {
        return createRandomReplyOf(owner, post, CreateReplyRequestBuilder.createRandom())
    }

    protected final ReplyInfo createRandomReplyOf(
            final CreatedUser owner,
            final PostBriefInfo post,
            final CreateReplyRequest replyRequest
    ) {
        final accessToken = owner.accessToken

        final rawResponse = authenticatedRequest(accessToken)
                .body(replyRequest)
                .post(ApiPathsHelper.POST_ID_REPLY(post.accessId.toString()))

        final response = expectResponse(rawResponse, HttpStatus.OK, ReplyInfoResponse.class)

        // Fixing groovyc error: reference problem in closures
        final replyRepo = replyRepo

        return txTemplate.execute {
            final reply = replyRepo.findByAccessId(UUID.fromString(response.replyId))

            return new ReplyInfo.Companion().from(reply)
        }
    }

    protected static ResponseFieldsSnippet replyInfoResponseFieldsDoc() {
        final List<FieldDescriptor> fields = [
                fieldWithPath("body.postId")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_POST_ID),
                fieldWithPath("body.replyId")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_REPLY_ID),
                fieldWithPath("body.writerNickname")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_WRITER_NICKNAME),
                fieldWithPath("body.writerLoginName")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_WRITER_LOGIN_NAME),
                fieldWithPath("body.lastModifiedDate")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_LAST_MODIFIED_DATE),
                fieldWithPath("body.lastModifiedIp")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_LAST_MODIFIED_IP),
                fieldWithPath("body.lastModifiedPlatformType")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_LAST_MODIFIED_PLATFORM_TYPE),
                fieldWithPath("body.edited")
                        .type(JsonFieldType.BOOLEAN)
                        .description(ReplyInfoResponse.DESC_EDITED),
                fieldWithPath("body.number")
                        .type(JsonFieldType.NUMBER)
                        .description(ReplyInfoResponse.DESC_NUMBER),
                fieldWithPath("body.contents")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_CONTENTS)
        ]

        return responseFields(basicFieldsDoc() + fields)
    }
}
