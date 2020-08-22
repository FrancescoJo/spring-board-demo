/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.board

import com.github.fj.board.persistence.model.board.BoardAccess
import com.github.fj.board.service.board.GetBoardService
import com.github.fj.board.service.board.impl.GetBoardServiceImpl
import com.github.fj.board.vo.board.BoardsSortBy
import com.github.fj.lib.collection.iterationsOf
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.data.domain.Sort
import test.com.github.fj.board.vo.auth.ClientAuthInfoBuilder
import test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Jul - 2020
 */
class GetBoardListServiceTest : AbstractBoardServiceTestTemplate() {
    private lateinit var sut: GetBoardService

    @BeforeEach
    override fun setup() {
        super.setup()
        this.sut = GetBoardServiceImpl(boardRepo)
    }

    @Test
    fun `anonymous users can get list of public boards`() {
        // given:
        val boards = 5.iterationsOf { randomBoardWithAccess(BoardAccess.PUBLIC) } +
                5.iterationsOf { randomBoardWithAccess(BoardAccess.MEMBERS_ONLY) }
        val sort = randomEnumConst(BoardsSortBy::class.java)
        val sortOrder = randomEnumConst(Sort.Direction::class.java)

        // when:
        `when`(boardRepo.findAllByAccess(BoardAccess.PUBLIC, sort.toPropertyName(), sortOrder))
            .thenReturn(boards)

        // then:
        val result = sut.getList(sort, sortOrder, null)

        // expect:
        assertThat(result.size, `is`(10))
    }

    @Test
    fun `authenticated user can get full list of boards`() {
        // given:
        val boards = 5.iterationsOf { randomBoardWithAccess(BoardAccess.PUBLIC) } +
                5.iterationsOf { randomBoardWithAccess(BoardAccess.MEMBERS_ONLY) }
        val sort = randomEnumConst(BoardsSortBy::class.java)
        val sortOrder = randomEnumConst(Sort.Direction::class.java)

        // when:
        `when`(boardRepo.findAll(any(), eq(Sort.by(sortOrder, sort.toPropertyName()))))
            .thenReturn(boards)

        // then:
        val result = sut.getList(sort, sortOrder, ClientAuthInfoBuilder.createRandom())

        // expect:
        assertThat(result.size, `is`(10))
    }
}
