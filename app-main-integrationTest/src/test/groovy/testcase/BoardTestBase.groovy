/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.dto.BoardInfoResponse
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.vo.board.BoardInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.transaction.support.TransactionTemplate
import test.endpoint.v1.board.dto.CreateBoardRequestBuilder

import static org.hamcrest.CoreMatchers.is
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
class BoardTestBase extends UserTestBase {
    @Autowired
    private BoardRepository boardRepo

    @Autowired
    private TransactionTemplate txTemplate

    protected final BoardInfo createRandomBoardBy(final CreatedUser owner) {
        final request = CreateBoardRequestBuilder.createRandom()

        final reqSpec = authenticatedRequest(owner.accessToken)
                .body(request)
                .post(ApiPaths.BOARD)

        final response = expectResponse(reqSpec.then().assertThat().statusCode(is(200)), BoardInfoResponse.class)

        // Fixing groovyc error: reference problem in closures
        final repository = boardRepo

        return txTemplate.execute {
            final board = repository.findByAccessId(UUID.fromString(response.accessId))
            return new BoardInfo.Companion().from(board)
        }
    }

    protected static ResponseFieldsSnippet boardInfoResponseFieldsDoc() {
        final List<FieldDescriptor> fields = [
                fieldWithPath("body.accessId")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_ACCESS_ID),
                fieldWithPath("body.status")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_STATUS),
                fieldWithPath("body.access")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_ACCESS),
                fieldWithPath("body.mode")
                        .type(JsonFieldType.STRING)
                        .description(BoardInfoResponse.DESC_MODE),
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
