/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.endpoint.ApiPathsHelper
import testcase.BoardTestBase

import static org.hamcrest.CoreMatchers.is

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
class GetBoardSingleSpec extends BoardTestBase {
    def "fail if accessId is not a UUID format"() {
        when:
        final reqSpec = sendRequest(
                "getBoardSingle-error-illegalAccessId",
                "__not-a-uuid-format__",
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "fail if board is not found for given accessId"() {
        when:
        final reqSpec = sendRequest(
                "getBoardSingle-error-notFound",
                UUID.randomUUID().toString(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(404))).body

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    def "fail if board is closed"() {
        given:
        final self = createRandomUser()
        final boardInfo = super.createRandomBoardBy(self)
        closeBoard(self, boardInfo.accessId)

        when:
        final reqSpec = sendRequest(
                "getBoardSingle-error-boardIsClosed",
                boardInfo.accessId.toString(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(404))).body

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    def "not authenticated user can not access to members only board"() {

    }

    def "not authenticated user can only access to public board"() {

    }

    def "authenticated user can access to public and members only board"() {

    }

    private Response sendRequest(
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

        return reqSpec.get(ApiPathsHelper.BOARD_ACCESS_ID(accessId))
    }
}
