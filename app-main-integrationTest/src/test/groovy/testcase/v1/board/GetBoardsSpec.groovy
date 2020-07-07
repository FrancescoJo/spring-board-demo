/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.dto.BoardsResponse
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.IntegrationTestBase

import static org.hamcrest.CoreMatchers.is
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

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
        final rawResponse = expectResponse(reqSpec.then().assertThat().statusCode(is(200)))
        final response = extractResponse(rawResponse, BoardsResponse.class)

        expect:
        response.boards.size() == 1
    }

    private static ResponseFieldsSnippet getBoardsResponseFields() {
        return responseFields(
                fieldWithPath("timestamp")
                        .type(JsonFieldType.NUMBER)
                        .description("Server timestamp"),
                fieldWithPath("type")
                        .type(JsonFieldType.STRING)
                        .description("Type of response"),
                fieldWithPath("body")
                        .type(JsonFieldType.OBJECT)
                        .description("Response payload"),
                fieldWithPath("body.boards")
                        .type(JsonFieldType.ARRAY)
                        .description("List of 'Board' object"),
                fieldWithPath("body.boards[].name")
                        .type(JsonFieldType.STRING)
                        .description("Board name")
        )
    }
}
