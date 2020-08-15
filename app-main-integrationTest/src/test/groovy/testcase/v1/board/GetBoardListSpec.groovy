/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.endpoint.common.FetchCriteriaRequestMixin
import com.github.fj.board.endpoint.v1.board.response.BoardInfoListResponse
import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.vo.board.BoardInfo
import com.github.fj.board.vo.board.BoardsSortBy
import com.github.fj.lib.collection.CollectionUtilsKt
import io.restassured.response.Response
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.com.github.fj.board.endpoint.v1.board.dto.CreateBoardRequestBuilder
import testcase.common.CreatedUser
import testcase.v1.BoardTestBase

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
class GetBoardListSpec extends BoardTestBase {
    def "not authenticated user can get list of public boards"() {
        given:
        CollectionUtilsKt.iterationsOf(2) { createBoardWithAccess(BoardAccess.PUBLIC) }
        CollectionUtilsKt.iterationsOf(2) { createBoardWithAccess(BoardAccess.MEMBERS_ONLY) }

        when:
        final rawResponse = unauthenticatedRequest("getBoardList-noAuth-membersOnly", boardInfoListResponseFieldsDoc())

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, BoardInfoListResponse.class)

        expect:
        response.boards.size() == 2
        !response.boards.any { it.access != BoardAccess.PUBLIC }
    }

    def "authenticated user can access to all boards"() {
        given:
        CollectionUtilsKt.iterationsOf(2) { createBoardWithAccess(BoardAccess.PUBLIC) }
        CollectionUtilsKt.iterationsOf(2) { createBoardWithAccess(BoardAccess.MEMBERS_ONLY) }

        when:
        final rawResponse = sendRequest(
                "getBoardList-auth-allBoards",
                createRandomUser().accessToken,
                boardInfoListResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, BoardInfoListResponse.class)

        expect:
        response.boards.size() == 4
    }

    def "only normal boards are present in list"() {
        given:
        final owner = createRandomUser()
        final createdBoards = CollectionUtilsKt.iterationsOf(4) {
            createBoardWithAccess(owner, BoardAccess.PUBLIC)
        }

        and:
        2.times { i ->
            updateBoardStatus(createdBoards[i].accessId, BoardStatus.CLOSED)
        }

        when:
        final rawResponse = sendRequest(
                "getBoardList-onlyNormalBoardsAreShown",
                owner.accessToken,
                boardInfoListResponseFieldsDoc()
        )

        then:
        final response = expectResponse(rawResponse, HttpStatus.OK, BoardInfoListResponse.class)

        expect: "2 of 4 are closed so it will be hidden in the list"
        response.boards.size() == 2
    }

    def "boards are sorted by given criteria"() {
        given: "name, by descending"
        final self = createRandomUser()
        final board2 = createBoardBy(new CreateBoardRequestBuilder(CreateBoardRequestBuilder.createRandom())
                .key("key2")
                .build()
        )
        final board1 = createBoardBy(new CreateBoardRequestBuilder(CreateBoardRequestBuilder.createRandom())
                .key("key1")
                .build()
        )

        when: "sorted by key, ascending by default"
        final rawResponse1 = jsonRequestSpec().get(ApiPaths.BOARDS)

        then:
        final response1 = expectResponse(rawResponse1, HttpStatus.OK, BoardInfoListResponse.class)

        expect:
        response1.boards[0].key == board1.key
        response1.boards[1].key == board2.key

        when: "sorted by key, descending"
        final rawResponse2 = authenticatedRequest(
                "getBoardList-sortedByCriteria",
                self.accessToken,
                boardInfoListResponseFieldsDoc()
        ).get("${ApiPaths.BOARDS}?${FetchCriteriaRequestMixin.GET_LIST_PARAM_SORT_BY}=${BoardsSortBy.KEY.value}&" +
                "${FetchCriteriaRequestMixin.GET_LIST_PARAM_ORDER_BY}=new")

        then:
        final response2 = expectResponse(rawResponse2, HttpStatus.OK, BoardInfoListResponse.class)

        expect:
        response2.boards[0].key == board2.key
        response2.boards[1].key == board1.key
    }

    private BoardInfo createBoardWithAccess(final BoardAccess access) {
        return createBoardBy(new CreateBoardRequestBuilder(CreateBoardRequestBuilder.createRandom())
                .access(access)
                .build())
    }

    private BoardInfo createBoardWithAccess(final CreatedUser owner, final BoardAccess access) {
        return createBoardOf(owner, new CreateBoardRequestBuilder(CreateBoardRequestBuilder.createRandom())
                .access(access)
                .build())
    }

    private Response unauthenticatedRequest(final String documentId, final ResponseFieldsSnippet respDoc) {
        return jsonRequestSpec(documentId, respDoc)
                .when()
                .get(ApiPaths.BOARDS)
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
