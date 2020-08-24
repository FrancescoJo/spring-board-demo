/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.endpoint.v1.board.response.BoardInfoResponse
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.vo.board.BoardInfo
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import spock.lang.Unroll
import test.com.github.fj.board.endpoint.ApiPathsHelper
import test.com.github.fj.board.endpoint.v1.board.dto.CreateBoardRequestBuilder
import testcase.v1.BoardTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
class GetBoardSingleSpec extends BoardTestBase {
    def "fail if accessId is not a UUID format"() {
        when:
        final response = unauthenticatedRequest(
                "getBoardSingle-error-illegalAccessId",
                "__not-a-uuid-format__",
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, IllegalRequestException.STATUS)

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "fail if board is not found for given accessId"() {
        when:
        final response = unauthenticatedRequest(
                "getBoardSingle-error-notFound",
                UUID.randomUUID().toString(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, BoardNotFoundException.STATUS)

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    def "fail if board is closed"() {
        given:
        final self = createRandomUser()
        final boardInfo = super.createRandomBoardOf(self)
        updateBoardStatus(boardInfo.accessId, BoardStatus.CLOSED)

        when:
        final response = unauthenticatedRequest(
                "getBoardSingle-error-boardIsClosed",
                boardInfo.accessId.toString(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, BoardNotFoundException.STATUS)

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    def "not authenticated user can not access to members only board"() {
        given:
        final board = randomBoardWithAccess(BoardAccess.MEMBERS_ONLY)

        when:
        final response = unauthenticatedRequest(
                "getBoardSingle-error-boardIsMembersOnly",
                board.accessId.toString(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(response, BoardNotFoundException.STATUS)

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    def "not authenticated user can only access to public board"() {
        given:
        final board = randomBoardWithAccess(BoardAccess.PUBLIC)

        when:
        final rawResponse = unauthenticatedRequest(
                "getBoardSingle-unauthenticated-publicOnly",
                board.accessId.toString(),
                boardInfoResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, BoardInfoResponse.class)

        expect:
        response.accessId == board.accessId.toString()
        response.access == BoardAccess.PUBLIC
    }

    @Unroll
    def "authenticated user can access to #access board"() {
        given:
        final self = createRandomUser()
        final board = randomBoardWithAccess(access)

        when:
        final rawResponse = sendRequest(
                "getBoardSingle-authenticated-$access",
                self.accessToken,
                board.accessId.toString(),
                boardInfoResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, BoardInfoResponse.class)

        expect:
        response.accessId == board.accessId.toString()
        response.access == access

        where:
        access                   | _
        BoardAccess.PUBLIC       | _
        BoardAccess.MEMBERS_ONLY | _
    }

    private BoardInfo randomBoardWithAccess(final BoardAccess access) {
        final boardSpec = new CreateBoardRequestBuilder(CreateBoardRequestBuilder.createRandom())
                .access(access)
                .build()

        return super.createBoardBy(boardSpec)
    }

    private Response unauthenticatedRequest(
            final String documentId,
            final String accessId,
            final ResponseFieldsSnippet respDoc
    ) {
        return sendRequest(documentId, null, accessId, respDoc)
    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final String accessId,
            final ResponseFieldsSnippet respDoc
    ) {
        final RequestSpecification reqSpec

        if (accessToken == null || accessToken.isEmpty()) {
            reqSpec = jsonRequestSpec(documentId, respDoc)
        } else {
            reqSpec = authenticatedRequest(documentId, accessToken, respDoc)
        }

        return reqSpec.get(ApiPathsHelper.BOARD_ID(accessId))
    }
}
