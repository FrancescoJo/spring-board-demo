/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.endpoint.v1.post.request

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext
import com.github.fj.board.endpoint.v1.post.request.AttachmentModeRequest
import com.github.fj.board.endpoint.v1.post.request.UpdateAttachmentsDeserialiser
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.core.IsNot.not
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import test.com.github.fj.board.endpoint.v1.post.dto.CreateAttachmentRequestBuilder
import test.com.github.fj.board.endpoint.v1.post.dto.DeleteAttachmentRequestBuilder
import test.com.github.fj.board.endpoint.v1.post.dto.UpdateAttachmentRequestBuilder
import java.util.stream.Stream

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
class UpdateAttachmentsDeserialiserTest {
    private lateinit var sut: UpdateAttachmentsDeserialiser

    @BeforeEach
    fun setup() {
        this.sut = UpdateAttachmentsDeserialiser()
    }

    @Test
    fun `injected ObjectMapper instance retains inside class`() {
        // given:
        val objMapper = ObjectMapper()

        // when:
        UpdateAttachmentsDeserialiser.injectMapper(objMapper)

        // then:
        assertThat(UpdateAttachmentsDeserialiser.objMapper, `is`(objMapper))
    }

    @Test
    fun `injecting different ObjectMapper twice takes no effect`() {
        // given:
        UpdateAttachmentsDeserialiser.injectMapper(ObjectMapper())

        // and:
        val objMapper = ObjectMapper()

        // when:
        UpdateAttachmentsDeserialiser.injectMapper(objMapper)

        // then:
        assertThat(UpdateAttachmentsDeserialiser.objMapper, not(objMapper))
    }

    @Test
    fun `parsing will fail if unparsable mode is provided`() {
        sut.afterPropertiesSet()
        // given:
        val content = """
            {
              "mode": "????"
            }
        """.trimIndent()
        val parser = ObjectMapper().factory.createParser(content)
        val context = DefaultDeserializationContext.Impl(BeanDeserializerFactory.instance)

        // expect:
        assertThrows<JsonParseException> {
            sut.deserialize(parser, context)
        }
    }

    @Test
    fun `parsing will fail if payload is empty`() {
        sut.afterPropertiesSet()
        // given:
        val content = """
            {
              "mode": "+"
            }
        """.trimIndent()
        val parser = ObjectMapper().factory.createParser(content)
        val context = DefaultDeserializationContext.Impl(BeanDeserializerFactory.instance)

        // expect:
        assertThrows<JsonParseException> {
            sut.deserialize(parser, context)
        }
    }

    @Test
    fun `parsing will fail if payload is in unexpected format`() {
        sut.afterPropertiesSet()
        // given:
        val content = """
            {
              "mode": "+",
              "payload": {
                "accessId": "----"
              }
            }
        """.trimIndent()
        val parser = ObjectMapper().factory.createParser(content)
        val context = DefaultDeserializationContext.Impl(BeanDeserializerFactory.instance)

        // expect:
        assertThrows<JsonParseException> {
            sut.deserialize(parser, context)
        }
    }

    @ParameterizedTest(name = "update attachment works in {0} mode")
    @MethodSource("testUpdateAttachmentRequest")
    fun `update attachment mode works as expected`(mode: AttachmentModeRequest, payload: Any) {
        sut.afterPropertiesSet()
        // given:
        val request = UpdateAttachmentRequestBuilder()
            .mode(mode)
            .payload(payload)
            .build()
        val content = ObjectMapper().writeValueAsString(request)
        val parser = ObjectMapper().factory.createParser(content)
        val context = DefaultDeserializationContext.Impl(BeanDeserializerFactory.instance)

        // when:
        val actual = sut.deserialize(parser, context)

        // then:
        assertThat(actual, `is`(request))
    }

    @AfterEach
    fun tearDown() {
        UpdateAttachmentsDeserialiser.objMapper = null
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testUpdateAttachmentRequest(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(AttachmentModeRequest.CREATE, CreateAttachmentRequestBuilder.createRandom()),
                Arguments.of(AttachmentModeRequest.DELETE, DeleteAttachmentRequestBuilder.createRandom())
            )
        }
    }
}
