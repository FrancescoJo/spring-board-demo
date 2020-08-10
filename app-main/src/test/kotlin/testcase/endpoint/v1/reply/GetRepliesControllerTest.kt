/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.endpoint.v1.reply

import com.github.fj.board.component.auth.ControllerClientAuthInfoDetector
import com.github.fj.board.endpoint.v1.reply.GetRepliesController
import com.github.fj.board.endpoint.v1.reply.GetRepliesControllerImpl
import com.github.fj.board.endpoint.v1.reply.dto.RepliesFetchCriteria
import com.github.fj.board.service.reply.GetRepliesService
import com.github.fj.board.service.reply.GetRepliesService.Companion.DEFAULT_REPLY_FETCH_SIZE
import com.github.fj.board.service.reply.GetRepliesService.Companion.MAXIMUM_REPLY_FETCH_SIZE
import com.github.fj.board.vo.reply.ReplyInfo
import com.github.fj.lib.util.getRandomAlphaNumericString
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.springframework.util.LinkedMultiValueMap
import test.com.github.fj.board.persistence.entity.reply.ReplyBuilder
import test.com.github.fj.board.vo.MockPageable
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Aug - 2020
 */
class GetRepliesControllerTest {
    @Mock
    private lateinit var authDetector: ControllerClientAuthInfoDetector

    @Mock
    private lateinit var svc: GetRepliesService

    private lateinit var sut: GetRepliesController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        this.sut = GetRepliesControllerImpl(authDetector, svc)
    }

    @Test
    fun `fetch criteria is set to default if no request parameters are given`() {
        // given:
        val mockHttpServletReq = mock(HttpServletRequest::class.java)
        val expected = RepliesFetchCriteria.DEFAULT_LATEST
        val postId = UUID.randomUUID()
        val svcResult = MockPageable<ReplyInfo>(
            0L,
            1000L,
            listOf(ReplyInfo.from(ReplyBuilder.createRandom()))
        )

        // when:
        `when`(authDetector.detectClientAuthInfo()).thenReturn(null)
        `when`(svc.getListOf(postId, null, expected)).thenReturn(svcResult)

        // then:
        sut.getLatest(postId.toString(), LinkedMultiValueMap<String, String>(), mockHttpServletReq)

        // expect:
        verify(svc, times(1)).getListOf(postId, null, expected)
    }

    @Test
    fun `page is defaulted to PAGE_LATEST if page is in illegal format or below 0`() {
        // given:
        val mockHttpServletReq = mock(HttpServletRequest::class.java)
        val postId = UUID.randomUUID()
        val expected = RepliesFetchCriteria.DEFAULT_LATEST
        val clientInfo = null
        val svcResult = MockPageable<ReplyInfo>(
            0L,
            1000L,
            listOf(ReplyInfo.from(ReplyBuilder.createRandom()))
        )

        // when:
        `when`(authDetector.detectClientAuthInfo()).thenReturn(clientInfo)
        `when`(svc.getListOf(postId, clientInfo, expected)).thenReturn(svcResult)

        // then:
        sut.getLatest(postId.toString(), LinkedMultiValueMap<String, String>(), mockHttpServletReq)

        // expect:
        val criteriaCaptor: KArgumentCaptor<RepliesFetchCriteria> = argumentCaptor()
        verify(svc, times(1)).getListOf(any(), anyOrNull(), criteriaCaptor.capture())
        val actualCriteria = criteriaCaptor.firstValue

        // and:
        assertThat(actualCriteria.page, `is`(GetRepliesService.PAGE_LATEST))
    }

    @ParameterizedTest(
        name = "count is defaulted as DEFAULT_REPLY_FETCH_SIZE..MAXIMUM_REPLY_FETCH_SIZE " +
                "if unexpected value('{0}') is given"
    )
    @ValueSource(
        strings = ["-1", "0", "1",
            (DEFAULT_REPLY_FETCH_SIZE - 1).toString(),
            DEFAULT_REPLY_FETCH_SIZE.toString(),
            (DEFAULT_REPLY_FETCH_SIZE + 1).toString(),
            (MAXIMUM_REPLY_FETCH_SIZE - 1).toString(),
            MAXIMUM_REPLY_FETCH_SIZE.toString(),
            (MAXIMUM_REPLY_FETCH_SIZE + 1).toString(),
            Integer.MAX_VALUE.toString(),
            "__not-a-decimal__"
        ]
    )
    fun `count is defaulted as DEFAULT_REPLY_FETCH_SIZE to MAXIMUM_REPLY_FETCH_SIZE`(count: String) {
        // given:
        val mockHttpServletReq = mock(HttpServletRequest::class.java)
        val svcResult = MockPageable<ReplyInfo>(
            0L,
            1000L,
            listOf(ReplyInfo.from(ReplyBuilder.createRandom()))
        )
        val params = LinkedMultiValueMap<String, String>().apply {
            put(GetRepliesController.GET_LIST_PARAM_COUNT, listOf(count))
        }

        // when:
        `when`(authDetector.detectClientAuthInfo()).thenReturn(null)
        `when`(svc.getListOf(any(), anyOrNull(), any())).thenReturn(svcResult)

        // then:
        sut.getLatest(UUID.randomUUID().toString(), params, mockHttpServletReq)

        // expect:
        val criteriaCaptor: KArgumentCaptor<RepliesFetchCriteria> = argumentCaptor()
        verify(svc, times(1)).getListOf(any(), anyOrNull(), criteriaCaptor.capture())
        val actualCriteria = criteriaCaptor.firstValue

        // and:
        assertTrue(actualCriteria.fetchSize in DEFAULT_REPLY_FETCH_SIZE..MAXIMUM_REPLY_FETCH_SIZE)
    }
}
