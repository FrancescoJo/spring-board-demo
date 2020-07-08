/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.ResponseFieldsSnippet

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

/**
 * This class holds documentation info of {@link GetBoardsSpec} test specifications, to reduce
 * test code verbosity. Yet in Groovy 2.5, interface with default method is not supported.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
class GetBoardsSpecDoc {
    static ResponseFieldsSnippet getBoardsResponseFields() {
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
