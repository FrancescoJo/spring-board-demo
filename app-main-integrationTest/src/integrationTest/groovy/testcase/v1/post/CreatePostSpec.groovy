/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.post

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.post.request.CreateAttachmentRequest
import com.github.fj.board.endpoint.v1.post.request.CreatePostRequest
import com.github.fj.board.endpoint.v1.post.response.PostInfoBriefResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.post.CannotCreatePostException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.persistence.model.board.BoardMode
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.lib.collection.CollectionUtilsKt
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import test.com.github.fj.board.endpoint.v1.board.dto.CreateBoardRequestBuilder
import test.com.github.fj.board.endpoint.v1.post.dto.CreateAttachmentRequestBuilder
import test.com.github.fj.board.endpoint.v1.post.dto.CreatePostRequestBuilder
import testcase.v1.PostTestBase

import static com.github.fj.lib.time.DateTimeUtilsKt.utcNow
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
class CreatePostSpec extends PostTestBase {
    def "fail if not authenticated"() {
        given:
        final request = CreatePostRequestBuilder.createRandom()

        when:
        final response = jsonRequestSpec("createPost-error-unauthenticated", requestFieldsDoc(), errorResponseFieldsDoc())
                .when()
                .body(request)
                .post(ApiPaths.BOARD)

        then:
        final errorBody = expectError(response, UnauthenticatedException.STATUS)

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if board for given boardId is not present"() {
        given:
        final self = createRandomUser()
        final request = CreatePostRequestBuilder.createRandom()
        final boardId = UUID.randomUUID()

        when:
        final response = sendRequest(
                "createPost-error-noBoardFound",
                self.accessToken,
                boardId.toString(),
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, BoardNotFoundException.STATUS)

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    @Unroll
    def "fail with #expectedException if board is in #boardStatus state"() {
        given:
        final self = createRandomUser()
        final createdBoard = createRandomBoardOf(self)
        updateBoardStatus(createdBoard.accessId, boardStatus)

        and:
        final request = CreatePostRequestBuilder.createRandom()
        final boardId = createdBoard.accessId

        when:
        final response = sendRequest(
                "createPost-error-illegalBoardStatus-#$docId",
                self.accessToken,
                boardId.toString(),
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, httpResponse)

        expect:
        errorBody.cause == expectedException

        where:
        boardStatus          | expectedException                    | httpResponse                     | docId
        BoardStatus.CLOSED   | BoardNotFoundException.simpleName    | BoardNotFoundException.STATUS    | 1
        BoardStatus.ARCHIVED | CannotCreatePostException.simpleName | CannotCreatePostException.STATUS | 2
    }

    def "fail if board for given boardId is in READ ONLY mode"() {
        given:
        final self = createRandomUser()
        final createdBoard = createRandomBoardOf(self)
        updateBoardMode(self, createdBoard, BoardMode.READ_ONLY)

        and:
        final request = CreatePostRequestBuilder.createRandom()

        when:
        final response = sendRequest(
                "createPost-error-readOnlyBoard",
                self.accessToken,
                createdBoard.accessId.toString(),
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, CannotCreatePostException.STATUS)

        expect:
        errorBody.cause == CannotCreatePostException.class.simpleName
    }

    def "more than 10 attachments are prohibited"() {
        given:
        final self = createRandomUser()
        final attachmentsSize = 11
        final createdBoard = createBoardOf(self, CreateBoardRequestBuilder.createRandom())
        final attachments = CollectionUtilsKt.iterationsOf(attachmentsSize) { CreateAttachmentRequestBuilder.createRandom() }
        final request = new CreatePostRequestBuilder(CreatePostRequestBuilder.createRandom())
            .attachments(attachments)
            .build()

        when:
        final response = sendRequest(
                "createPost-error-tooManyAttachments",
                self.accessToken,
                createdBoard.accessId.toString(),
                request,
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "success if request is valid"() {
        given:
        final self = createRandomUser()
        final createdBoard = createBoardOf(self, CreateBoardRequestBuilder.createRandom())
        final request = CreatePostRequestBuilder.createRandom()

        when:
        final rawResponse = sendRequest(
                "createPost",
                self.accessToken,
                createdBoard.accessId.toString(),
                request,
                postInfoBriefResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, PostInfoBriefResponse.class)

        expect: "Expecting a firstly created post"
        response.boardId == createdBoard.accessId.toString()
        response.postMode == request.mode
        response.postNumber == 1
        response.writerNickname == self.nickname
        response.writerLoginName == self.loginName
        response.lastModifiedDate <= utcNow()
        response.title == request.title
        response.viewCount == 0
    }

    def "consecutive posting increases post number in board"() {
        given:
        final self = createRandomUser()
        final createdBoard = createBoardOf(self, CreateBoardRequestBuilder.createRandom())
        final firstRandomPost = createRandomPostOf(self, createdBoard)

        expect:
        firstRandomPost.number == 1

        when:
        final secondRandomPost = createRandomPostOf(self, createdBoard)

        then:
        secondRandomPost.number == 2
    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final String boardId,
            final CreatePostRequest req,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, requestFieldsDoc(), respDoc)
                .body(req)
                .post(ApiPathsHelper.BOARD_ID_POST(boardId))
    }

    private static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("mode")
                        .type(JsonFieldType.STRING)
                        .description(CreatePostRequest.DESC_MODE),
                fieldWithPath("title")
                        .type(JsonFieldType.STRING)
                        .description(CreatePostRequest.DESC_TITLE),
                fieldWithPath("contents")
                        .type(JsonFieldType.STRING)
                        .description(CreatePostRequest.DESC_CONTENTS),
                fieldWithPath("attachments[]")
                        .optional()
                        .type(JsonFieldType.ARRAY)
                        .description(CreatePostRequest.DESC_ATTACHMENTS),
                fieldWithPath("attachments[].uri")
                        .type(JsonFieldType.STRING)
                        .description(CreateAttachmentRequest.DESC_URI),
                fieldWithPath("attachments[].mimeType")
                        .type(JsonFieldType.STRING)
                        .description(CreateAttachmentRequest.DESC_MIME_TYPE),
                fieldWithPath("attachments[].name")
                        .type(JsonFieldType.STRING)
                        .description(CreateAttachmentRequest.DESC_NAME)
        )
    }
}
