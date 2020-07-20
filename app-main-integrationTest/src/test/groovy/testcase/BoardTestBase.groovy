/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase

import com.github.fj.board.endpoint.v1.board.dto.BoardInfoResponse
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.ResponseFieldsSnippet

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
class BoardTestBase extends UserTestBase {
    protected static ResponseFieldsSnippet boardInfoResponseFieldsDoc() {
        final List<FieldDescriptor> fields = [
                fieldWithPath("body.accessId")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_ACCESS_ID),
                fieldWithPath("body.key")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_KEY),
                fieldWithPath("body.name")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_NAME),
                fieldWithPath("body.description")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_DESCRIPTION),
                fieldWithPath("body.postsCount")
                        .type(JsonFieldType.NUMBER)
                        .description(BoardInfoResponse.DESC_POSTS_COUNT),
                fieldWithPath("body.createdDate")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_CREATED_DATE),
                fieldWithPath("body.modifiedDate")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_MODIFIED_DATE),
                fieldWithPath("body.creatorLoginName")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_CREATOR_LOGIN_NAME)
        ]

        return responseFields(basicFieldsDoc() + fields)
    }
}
