/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1

import com.github.fj.board.endpoint.v1.post.request.CreatePostRequest
import com.github.fj.board.endpoint.v1.post.response.AttachmentInfoResponse
import com.github.fj.board.endpoint.v1.post.response.PostInfoBriefResponse
import com.github.fj.board.endpoint.v1.post.response.PostInfoDetailedResponse
import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.persistence.model.post.PostMode
import com.github.fj.board.persistence.repository.post.AttachmentRepository
import com.github.fj.board.persistence.repository.post.PostRepository
import com.github.fj.board.vo.board.BoardInfo
import com.github.fj.board.vo.post.PostDetailedInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.transaction.support.TransactionTemplate
import test.com.github.fj.board.endpoint.ApiPathsHelper
import test.com.github.fj.board.endpoint.v1.post.dto.CreatePostRequestBuilder
import testcase.common.CreatedUser

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
class PostTestBase extends BoardTestBase {
    @Autowired
    private PostRepository postRepo

    @Autowired
    private AttachmentRepository attachmentRepo

    @Autowired
    private TransactionTemplate txTemplate

    def cleanup() {
        attachmentRepo.deleteAll()
        postRepo.deleteAll()
    }

    protected final PostDetailedInfo createRandomPostOf(final CreatedUser owner, final BoardInfo board) {
        return createRandomPostOf(owner, board, CreatePostRequestBuilder.createRandom())
    }

    protected final PostDetailedInfo createRandomPostOf(
            final CreatedUser owner,
            final BoardInfo board,
            final CreatePostRequest postRequest
    ) {
        final accessToken = owner.accessToken

        final rawResponse = authenticatedRequest(accessToken)
                .body(postRequest)
                .post(ApiPathsHelper.BOARD_ID_POST(board.accessId.toString()))

        final response = expectResponse(rawResponse, HttpStatus.OK, PostInfoBriefResponse.class)

        // Fixing groovyc error: reference problem in closures
        final postRepo = postRepo
        final attachmentRepo = attachmentRepo

        return txTemplate.execute {
            final post = postRepo.findByAccessId(UUID.fromString(response.postId))
            final attachments = attachmentRepo.findAllByPost(post)

            return new PostDetailedInfo.Companion().from(post, 0L, attachments)
        }
    }

    protected final void updatePostMode(final CreatedUser owner, final PostDetailedInfo post, final PostMode mode) {
        // Fixing groovyc error: reference problem in closures
        final postRepo = postRepo

        txTemplate.execute {
            final postEntity = postRepo.findByAccessId(post.accessId)
            postEntity.setMode(mode)
        }
    }

    protected static ResponseFieldsSnippet postInfoBriefResponseFieldsDoc() {
        return responseFields(basicFieldsDoc() + postInfoBriefResponseFields("body"))
    }

    protected static List<FieldDescriptor> postInfoBriefResponseFields(final String prefix) {
        return [
                fieldWithPath("${prefix}.boardId")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_BOARD_ID),
                fieldWithPath("${prefix}.postId")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_POST_ID),
                fieldWithPath("${prefix}.postMode")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_POST_MODE),
                fieldWithPath("${prefix}.postNumber")
                        .type(JsonFieldType.NUMBER)
                        .description(PostInfoBriefResponse.DESC_POST_NUMBER),
                fieldWithPath("${prefix}.writerNickname")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_WRITER_NICKNAME),
                fieldWithPath("${prefix}.writerLoginName")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_WRITER_LOGIN_NAME),
                fieldWithPath("${prefix}.lastModifiedDate")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_LAST_MODIFIED_DATE),
                fieldWithPath("${prefix}.title")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_TITLE),
                fieldWithPath("${prefix}.viewCount")
                        .type(JsonFieldType.NUMBER)
                        .description(PostInfoBriefResponse.DESC_VIEW_COUNT),
                fieldWithPath("${prefix}.replyCount")
                        .type(JsonFieldType.NUMBER)
                        .description(PostInfoBriefResponse.DESC_REPLY_COUNT)
        ]
    }

    protected static ResponseFieldsSnippet postInfoDetailedResponseFieldsDoc() {
        final List<FieldDescriptor> fields = [
                fieldWithPath("body.boardId")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoDetailedResponse.DESC_BOARD_ID),
                fieldWithPath("body.postId")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoDetailedResponse.DESC_POST_ID),
                fieldWithPath("body.postMode")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoDetailedResponse.DESC_POST_MODE),
                fieldWithPath("body.postNumber")
                        .type(JsonFieldType.NUMBER)
                        .description(PostInfoDetailedResponse.DESC_POST_NUMBER),
                fieldWithPath("body.writerNickname")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(PostInfoDetailedResponse.DESC_WRITER_NICKNAME),
                fieldWithPath("body.writerLoginName")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(PostInfoDetailedResponse.DESC_WRITER_LOGIN_NAME),
                fieldWithPath("body.lastModifiedDate")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoDetailedResponse.DESC_LAST_MODIFIED_DATE),
                fieldWithPath("body.lastModifiedIp")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoDetailedResponse.DESC_LAST_MODIFIED_IP),
                fieldWithPath("body.lastModifiedPlatformType")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoDetailedResponse.DESC_LAST_MODIFIED_PLATFORM_TYPE),
                fieldWithPath("body.isEdited")
                        .type(JsonFieldType.BOOLEAN)
                        .description(PostInfoDetailedResponse.DESC_EDITED),
                fieldWithPath("body.number")
                        .type(JsonFieldType.NUMBER)
                        .description(PostInfoDetailedResponse.DESC_NUMBER),
                fieldWithPath("body.title")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoDetailedResponse.DESC_TITLE),
                fieldWithPath("body.contents")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoDetailedResponse.DESC_CONTENTS),
                fieldWithPath("body.viewCount")
                        .type(JsonFieldType.NUMBER)
                        .description(PostInfoDetailedResponse.DESC_VIEW_COUNT),
                fieldWithPath("body.attachments[]")
                        .type(JsonFieldType.ARRAY)
                        .description(PostInfoDetailedResponse.DESC_ATTACHMENTS),
                fieldWithPath("body.attachments[].postId")
                        .type(JsonFieldType.STRING)
                        .description(AttachmentInfoResponse.DESC_POST_ID),
                fieldWithPath("body.attachments[].accessId")
                        .type(JsonFieldType.STRING)
                        .description(AttachmentInfoResponse.DESC_ATTACHMENT_ID),
                fieldWithPath("body.attachments[].uri")
                        .type(JsonFieldType.STRING)
                        .description(AttachmentInfoResponse.DESC_URI),
                fieldWithPath("body.attachments[].mimeType")
                        .type(JsonFieldType.STRING)
                        .description(AttachmentInfoResponse.DESC_MIME_TYPE),
                fieldWithPath("body.attachments[].name")
                        .type(JsonFieldType.STRING)
                        .description(AttachmentInfoResponse.DESC_NAME),
                fieldWithPath("body.replies")
                        .type(JsonFieldType.OBJECT)
                        .description(PostInfoDetailedResponse.DESC_REPLIES),
        ] + pageableResponseFields("body.replies", replyInfoResponseFields("body.replies.data[]"))

        return responseFields(basicFieldsDoc() + fields)
    }

    protected static List<FieldDescriptor> replyInfoResponseFields(final String prefix) {
        return [
                fieldWithPath("${prefix}.postId")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_POST_ID),
                fieldWithPath("${prefix}.replyId")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_REPLY_ID),
                fieldWithPath("${prefix}.writerNickname")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_WRITER_NICKNAME),
                fieldWithPath("${prefix}.writerLoginName")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_WRITER_LOGIN_NAME),
                fieldWithPath("${prefix}.lastModifiedDate")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_LAST_MODIFIED_DATE),
                fieldWithPath("${prefix}.lastModifiedIp")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_LAST_MODIFIED_IP),
                fieldWithPath("${prefix}.lastModifiedPlatformType")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_LAST_MODIFIED_PLATFORM_TYPE),
                fieldWithPath("${prefix}.edited")
                        .type(JsonFieldType.BOOLEAN)
                        .description(ReplyInfoResponse.DESC_EDITED),
                fieldWithPath("${prefix}.number")
                        .type(JsonFieldType.NUMBER)
                        .description(ReplyInfoResponse.DESC_NUMBER),
                fieldWithPath("${prefix}.contents")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_CONTENTS)
        ]
    }
}
