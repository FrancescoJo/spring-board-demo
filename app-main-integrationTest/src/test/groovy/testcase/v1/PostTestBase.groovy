/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1

import com.github.fj.board.endpoint.v1.post.request.CreatePostRequest
import com.github.fj.board.endpoint.v1.post.response.PostInfoBriefResponse
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

            return new PostDetailedInfo.Companion().from(post, attachments)
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
        final List<FieldDescriptor> fields = [
                fieldWithPath("body.boardId")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_BOARD_ID),
                fieldWithPath("body.postId")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_POST_ID),
                fieldWithPath("body.postMode")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_POST_MODE),
                fieldWithPath("body.postNumber")
                        .type(JsonFieldType.NUMBER)
                        .description(PostInfoBriefResponse.DESC_POST_NUMBER),
                fieldWithPath("body.writerNickname")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_WRITER_NICKNAME),
                fieldWithPath("body.writerLoginName")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_WRITER_LOGIN_NAME),
                fieldWithPath("body.lastModifiedDate")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_LAST_MODIFIED_DATE),
                fieldWithPath("body.title")
                        .type(JsonFieldType.STRING)
                        .description(PostInfoBriefResponse.DESC_TITLE),
                fieldWithPath("body.viewCount")
                        .type(JsonFieldType.NUMBER)
                        .description(PostInfoBriefResponse.DESC_VIEW_COUNT)
        ]

        return responseFields(basicFieldsDoc() + fields)
    }
}
