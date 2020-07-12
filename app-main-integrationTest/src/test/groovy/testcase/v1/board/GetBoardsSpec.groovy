/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.dto.BoardsResponse
import testcase.IntegrationTestBase

import static org.hamcrest.CoreMatchers.is
import static testcase.v1.board.GetBoardsSpecDoc.getBoardsResponseFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
class GetBoardsSpec extends IntegrationTestBase {
    def "GET /boards displays all post boards"() {
        when:
        final reqSpec = jsonRequestSpec("get-boards", getBoardsResponseFields())
                .when()
                .get(ApiPaths.BOARDS)

        then:
        final response = expectResponse(reqSpec.then().assertThat().statusCode(is(200)), BoardsResponse.class)

        expect:
        response.boards.size() == 1
    }
}
