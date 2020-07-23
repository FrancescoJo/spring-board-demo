/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.v1.board.dto.BoardInfoResponse
import com.github.fj.board.endpoint.v1.board.dto.CreateBoardRequest
import com.github.fj.board.persistence.repository.board.BoardRepository
import com.github.fj.board.vo.board.BoardInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.transaction.support.TransactionTemplate
import test.endpoint.ApiPathsHelper
import test.endpoint.v1.board.dto.CreateBoardRequestBuilder

import javax.annotation.Nullable

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

    protected final BoardInfo createRandomBoardOf(final CreatedUser owner) {
        return createBoardAs(CreateBoardRequestBuilder.createRandom(), owner)
    }

    protected final BoardInfo createBoardBy(final CreateBoardRequest request) {
        return createBoardAs(request, null)
    }

    protected final BoardInfo createBoardAs(final CreateBoardRequest request, final @Nullable CreatedUser owner) {
        final String accessToken
        if (owner == null) {
            accessToken = createRandomUser().accessToken
        } else {
            accessToken = owner.accessToken
        }

        final response = authenticatedRequest(accessToken)
                .body(request)
                .post(ApiPaths.BOARD)

        final boardInfoResponse = expectResponse(response, HttpStatus.OK, BoardInfoResponse.class)

        // Fixing groovyc error: reference problem in closures
        final repository = boardRepo

        return txTemplate.execute {
            final board = repository.findByAccessId(UUID.fromString(boardInfoResponse.accessId))
            return new BoardInfo.Companion().from(board)
        }
    }

    protected final Boolean closeBoard(final CreatedUser owner, final UUID accessId) {
        final response = authenticatedRequest(owner.accessToken)
                .delete(ApiPathsHelper.BOARD_ACCESS_ID(accessId.toString()))

        final okResult = expectGenericResponse(response, HttpStatus.OK, Boolean.class)

        return okResult
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
