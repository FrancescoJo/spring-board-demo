/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board


import io.restassured.response.Response
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.endpoint.ApiPathsHelper
import testcase.BoardTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
class GetBoardSingleSpec extends BoardTestBase {
    def "fail if accessId is not a UUID format"() {

    }

    def "fail if board is not found for given accessId"() {

    }

    def "fail if board is not in normal state"() {

    }

    def "fail if board is for members only and user is not authenticated"() {

    }

    def "not authenticated user can only access to public board"() {

    }

    def "authenticated user can access to public and members only board"() {

    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final String accessId,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, respDoc)
                .get(ApiPathsHelper.BOARD_ACCESS_ID(accessId))
    }
}
