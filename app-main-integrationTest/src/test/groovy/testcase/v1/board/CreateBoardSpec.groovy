/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.endpoint.v1.board.dto.CreateBoardRequest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet
import testcase.BoardTestBase

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
class CreateBoardSpec extends BoardTestBase {
    def "fail if not authenticated"() {

    }

    def "fail if no user is created for authentication"() {

    }

    def "fail if board key('#key') is out of pattern"() {

    }

    def "fail if board name('#name') is too short or too long"() {

    }

    def "board is created if request is valid"() {

    }

    def "warning if duplicated board name is given"() {

    }

    private static RequestFieldsSnippet requestFieldsDoc() {
        return requestFields(
                fieldWithPath("key")
                        .type(JsonFieldType.STRING)
                        .description(CreateBoardRequest.DESC_KEY),
                fieldWithPath("name")
                        .type(JsonFieldType.STRING)
                        .description(CreateBoardRequest.DESC_NAME),
                fieldWithPath("description")
                        .type(JsonFieldType.STRING)
                        .description(CreateBoardRequest.DESC_DESCRIPTION)
        )
    }
}
