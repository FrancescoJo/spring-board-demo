/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1.board

import com.github.fj.board.endpoint.ApiPaths
import com.github.fj.board.exception.client.IllegalRequestException
import com.github.fj.board.exception.client.board.BoardNotFoundException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.exception.generic.UnauthenticatedException
import com.github.fj.board.exception.generic.UnauthorisedException
import com.github.fj.board.persistence.model.board.Status
import com.github.fj.board.persistence.repository.board.BoardRepository
import io.restassured.response.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import test.endpoint.ApiPathsHelper
import testcase.BoardTestBase

import static org.hamcrest.CoreMatchers.is

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Jul - 2020
 */
class CloseBoardSpec extends BoardTestBase {
    @Autowired
    private BoardRepository boardRepo

    def "fail if not authenticated"() {
        when:
        final reqSpec = jsonRequestSpec("closeBoard-error-unauthenticated", errorResponseFieldsDoc())
                .when()
                .delete(ApiPaths.BOARD)

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(401))).body

        expect:
        errorBody.cause == UnauthenticatedException.class.simpleName
    }

    def "fail if accessId is not a UUID format"() {
        given:
        final createdAuth = createRandomAuth()

        when:
        final reqSpec = sendRequest(
                "closeBoard-error-illegalAccessId",
                createdAuth.accessToken.value,
                "__not-a-uuid-format__",
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(400))).body

        expect:
        errorBody.cause == IllegalRequestException.class.simpleName
    }

    def "fail if no user is created for authentication"() {
        given:
        final createdAuth = createRandomAuth()

        when:
        final reqSpec = sendRequest(
                "closeBoard-error-noUserCreated",
                createdAuth.accessToken.value,
                UUID.randomUUID().toString(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(404))).body

        expect:
        errorBody.cause == UserNotFoundException.class.simpleName
    }

    def "fail if board is not found for given accessId"() {
        given:
        final self = createRandomUser()

        when:
        final reqSpec = sendRequest(
                "closeBoard-error-noBoardFound",
                self.accessToken,
                UUID.randomUUID().toString(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(404))).body

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    def "cannot close board which is already closed"() {
        given:
        final self = createRandomUser()
        final board = createRandomBoardBy(self)

        and:
        final boardEntity = boardRepo.findByAccessId(board.accessId)
        boardEntity.status = Status.CLOSED
        boardRepo.save(boardEntity)

        when:
        final reqSpec = sendRequest(
                "closeBoard-error-cannotCloseAlreadyClosed",
                self.accessToken,
                board.accessId.toString(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(404))).body

        expect:
        errorBody.cause == BoardNotFoundException.class.simpleName
    }

    def "only owner of board can close it"() {
        given:
        final self = createRandomUser()
        final notOwnedBoard = createRandomBoardBy(createRandomUser())

        when:
        final reqSpec = sendRequest(
                "closeBoard-error-boardNotOwned",
                self.accessToken,
                notOwnedBoard.accessId.toString(),
                errorResponseFieldsDoc()
        )

        then:
        final errorBody = expectError(reqSpec.then().assertThat().statusCode(is(403))).body

        expect:
        errorBody.cause == UnauthorisedException.class.simpleName
    }

    def "board is closed if request is valid"() {
        given:
        final self = createRandomUser()
        final ownedBoard = createRandomBoardBy(self)

        when:
        final reqSpec = sendRequest(
                "closeBoard",
                self.accessToken,
                ownedBoard.accessId.toString(),
                genericBooleanResponseDoc()
        )

        then:
        final okResult = expectGenericResponse(reqSpec.then().assertThat().statusCode(is(200)), Boolean.class)

        expect: "Normal result: true"
        okResult
    }

    private Response sendRequest(
            final String documentId,
            final String accessToken,
            final String accessId,
            final ResponseFieldsSnippet respDoc
    ) {
        return authenticatedRequest(documentId, accessToken, respDoc)
                .delete(ApiPathsHelper.BOARD_ACCESS_ID(accessId))
    }
}
