/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.post

import com.github.fj.board.endpoint.v1.post.request.AttachmentModeRequest
import com.github.fj.board.endpoint.v1.post.request.DeleteAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.UpdateAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.UpdatePostRequest
import com.github.fj.board.endpoint.v1.post.response.PostInfoBriefResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.AttachmentNotFoundException
import com.github.fj.board.exception.client.post.CannotEditPostException
import com.github.fj.board.exception.client.post.PostNotFoundException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.service.post.PostEditingServiceMixin
import com.github.fj.board.vo.board.BoardInfo
import com.github.fj.board.vo.post.PostDetailedInfo
import com.github.fj.lib.collection.CollectionUtilsKt
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import test.com.github.fj.board.endpoint.v1.post.dto.UpdateAttachmentRequestBuilder
import test.com.github.fj.board.endpoint.v1.post.dto.UpdatePostRequestBuilder
import testcase.common.CreatedUser
import testcase.v1.PostTestBase

import static org.springframework.restdocs.payload.PayloadDocumentation.*

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
class UpdatePostSpec extends PostTestBase {
    private CreatedUser self
    private BoardInfo board
    private PostDetailedInfo post

    def setup() {
        this.self = createRandomUser()
        this.board = createRandomBoardOf(self)
        this.post = createRandomPostOf(self, board)
    }

    def "fail if not authenticated"() {
        given:
        final request = UpdatePostRequestBuilder.createRandom()

        when:
        final response = jsonRequestSpec("updatePost-error-unauthenticated", genericRequestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .post(currentRequestUrl())

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if target post is not found"() {
        given:
        final request = UpdatePostRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updatePost-error-noPostFound",
                requestUrl(UUID.randomUUID()),
                request,
                genericRequestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, PostNotFoundException.STATUS)

        expect:
        errorBody.cause == PostNotFoundException.class.simpleName
    }

    def "fail if target post is not owned"() {
        given:
        final otherUserPost = createRandomPostOf(createRandomUser(), board)
        final request = UpdatePostRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updatePost-error-otherUserPost",
                requestUrl(otherUserPost.accessId),
                request,
                genericRequestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotEditPostException.STATUS)

        expect:
        errorBody.cause == CannotEditPostException.class.simpleName
    }

    @Unroll
    def "fail with #expectedException if board is in #boardStatus state"() {
        given:
        updateBoardStatus(board.accessId, boardStatus)
        final request = UpdatePostRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updatePost-error-illegalBoardStatus-#$docId",
                currentRequestUrl(),
                request,
                genericRequestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, httpResponse)

        expect:
        errorBody.cause == expectedException

        where:
        boardStatus          | expectedException                  | httpResponse                   | docId
        BoardStatus.CLOSED   | BoardNotFoundException.simpleName  | BoardNotFoundException.STATUS  | 1
        BoardStatus.ARCHIVED | CannotEditPostException.simpleName | CannotEditPostException.STATUS | 2
    }

    def "fail if board for given boardId is in READ ONLY mode"() {
        given:
        updateBoardMode(self, board, BoardMode.READ_ONLY)

        and:
        final request = UpdatePostRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "updatePost-error-readOnlyBoard",
                currentRequestUrl(),
                request,
                genericRequestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotEditPostException.STATUS)

        expect:
        errorBody.cause == CannotEditPostException.class.simpleName
    }

    def "fail if target attachments for deletion are not found"() {
        given:
        final request = new UpdatePostRequestBuilder(UpdatePostRequestBuilder.createRandom())
                .attachments(UpdateAttachmentRequestBuilder.createRandomBulk(AttachmentModeRequest.DELETE))
                .build()

        when:
        final response = sendRequest(
                "updatePost-error-noAttachmentFound",
                currentRequestUrl(),
                request,
                deleteAttachmentsRequestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, AttachmentNotFoundException.STATUS)

        expect:
        errorBody.cause == AttachmentNotFoundException.class.simpleName
    }

    def "fail if total count of attachments exceeds 10"() {
        given:
        final additionalAttachments = CollectionUtilsKt.iterationsOf(PostEditingServiceMixin.MAXIMUM_ATTACHMENTS_PER_POST) {
            UpdateAttachmentRequestBuilder.createRandom(AttachmentModeRequest.CREATE)
        }
        final request = new UpdatePostRequestBuilder(UpdatePostRequestBuilder.createRandom())
                .attachments(additionalAttachments)
                .build()

        when:
        final response = sendRequest(
                "updatePost-error-tooManyAttachments",
                currentRequestUrl(),
                request,
                genericRequestFieldsDoc(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "post is updated if request is valid"() {
        given:
        final request = new UpdatePostRequestBuilder(UpdatePostRequestBuilder.createRandom())
                .attachments(Collections.emptyList())
                .build()

        when:
        final rawResponse = sendRequest(
                "updatePost",
                currentRequestUrl(),
                request,
                genericRequestFieldsDoc(),
                postInfoBriefResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, PostInfoBriefResponse.class)

        expect:
        response.boardId == board.accessId.toString()
        response.postId == post.accessId.toString()
        response.postMode == request.mode
        response.title == request.title
    }

    private String currentRequestUrl() {
        return requestUrl(post.accessId)
    }

    private static String requestUrl(final UUID post) {
        return ApiPathsHelper.POST_ID(post.toString())
    }

    private Response sendRequest(
            final String documentId,
            final String endPoint,
            final UpdatePostRequest request,
            final RequestFieldsSnippet reqDoc,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, self.accessToken, reqDoc, respDoc)
                .body(request)
                .patch(endPoint)
    }

    private static RequestFieldsSnippet genericRequestFieldsDoc() {
        return requestFields(
                fieldWithPath("mode")
                        .type(JsonFieldType.STRING)
                        .description(UpdatePostRequest.DESC_MODE),
                fieldWithPath("title")
                        .type(JsonFieldType.STRING)
                        .description(UpdatePostRequest.DESC_TITLE),
                fieldWithPath("contents")
                        .type(JsonFieldType.STRING)
                        .description(UpdatePostRequest.DESC_CONTENTS),
                subsectionWithPath("attachments[]")
                        .optional()
                        .type(JsonFieldType.ARRAY)
                        .description(UpdatePostRequest.DESC_ATTACHMENTS)
        )
    }

    private static RequestFieldsSnippet deleteAttachmentsRequestFieldsDoc() {
        return requestFields(
                fieldWithPath("mode")
                        .type(JsonFieldType.STRING)
                        .description(UpdatePostRequest.DESC_MODE),
                fieldWithPath("title")
                        .type(JsonFieldType.STRING)
                        .description(UpdatePostRequest.DESC_TITLE),
                fieldWithPath("contents")
                        .type(JsonFieldType.STRING)
                        .description(UpdatePostRequest.DESC_CONTENTS),
                fieldWithPath("attachments[]")
                        .optional()
                        .type(JsonFieldType.ARRAY)
                        .description(UpdatePostRequest.DESC_ATTACHMENTS),
                fieldWithPath("attachments[].mode")
                        .type(JsonFieldType.STRING)
                        .description(UpdateAttachmentRequest.DESC_MODE),
                fieldWithPath("attachments[].payload")
                        .type(JsonFieldType.OBJECT)
                        .description(UpdateAttachmentRequest.DESC_PAYLOAD),
                fieldWithPath("attachments[].payload.accessId")
                        .type(JsonFieldType.STRING)
                        .description(DeleteAttachmentRequest.DESC_ACCESS_ID),
        )
    }
}
