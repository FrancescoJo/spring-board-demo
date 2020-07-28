/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.board

import com.github.fj.board.exception.client.board.DuplicatedBoardKeyException
import com.github.fj.board.exception.client.user.UserNotFoundException
import com.github.fj.board.persistence.model.board.BoardStatus
import com.github.fj.board.service.board.CreateBoardService
import com.github.fj.board.service.board.impl.CreateBoardServiceImpl
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import test.com.github.fj.board.endpoint.v1.board.dto.CreateBoardRequestBuilder
import test.com.github.fj.board.persistence.entity.board.BoardBuilder
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
class CreateBoardServiceTest : AbstractBoardServiceTestTemplate() {
    private lateinit var sut: CreateBoardService

    @BeforeEach
    override fun setup() {
        super.setup()
        this.sut = CreateBoardServiceImpl(userRepo, boardRepo)
    }

    @Test
    fun `fail if there is no User but only authentication`() {
        // given:
        val clientInfo = ClientAuthInfoBuilder.createRandom()
        val req = CreateBoardRequestBuilder.createRandom()

        // when:
        `when`(userRepo.findByLoginName(clientInfo.loginName)).thenReturn(null)

        // then:
        assertThrows<UserNotFoundException> {
            sut.create(req, clientInfo)
        }
    }

    @Test
    fun `fail if key is already exist`() {
        // given:
        val (authInfo, _) = prepareSelf()
        val req = CreateBoardRequestBuilder.createRandom()
        val board = BoardBuilder(BoardBuilder.createRandom())
            .key(req.key)
            .build()

        // when:
        `when`(boardRepo.findByKey(req.key)).thenReturn(board)

        // then:
        assertThrows<DuplicatedBoardKeyException> {
            sut.create(req, authInfo)
        }
    }

    @Test
    fun `board is created if request is valid`() {
        // given:
        val (authInfo, user) = prepareSelf()
        val req = CreateBoardRequestBuilder.createRandom()

        // then:
        val result = sut.create(req, authInfo)

        // expect:
        assertThat(result.status, `is`(BoardStatus.NORMAL))
        assertThat(result.key, `is`(req.key))
        assertThat(result.name, `is`(req.name))
        assertThat(result.description, `is`(req.description))
        assertThat(result.creator.loginName, `is`(user.authentication.loginName))
    }
}
