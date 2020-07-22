/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.endpoint.ApiPaths
import io.restassured.response.Response
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.BoardTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
class GetBoardListSpec extends BoardTestBase {
    def "only boards in normal state are present in list"() {

    }

    def "not authenticated user can only access to public boards"() {

    }

    def "authenticated user can access to all boards"() {

    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, respDoc)
                .get(ApiPaths.BOARDS)
    }
}
