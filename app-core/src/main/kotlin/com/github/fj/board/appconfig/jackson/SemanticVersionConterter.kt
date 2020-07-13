/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.github.fj.board.exception.client.IllegalRequestException
import de.skuzzle.semantic.Version

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
internal class SemanticVersionJacksonSerialiser : StdSerializer<Version>(Version::class.java) {
    override fun serialize(value: Version, gen: JsonGenerator, serializers: SerializerProvider) {
        serializers.defaultSerializeValue(value.toString(), gen)
    }
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
internal class SemanticVersionJacksonDeserialiser : JsonDeserializer<Version>(), ContextualDeserializer {
    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty): JsonDeserializer<*> =
        SemanticVersionJacksonDeserialiser()

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Version =
        // Happens only by client, thus we convert exception to IllegalRequestException here.
        @Suppress("TooGenericExceptionCaught")
        try {
            Version.parseVersion(p.valueAsString)
        } catch (e: RuntimeException) {
            throw IllegalRequestException("Cannot parse '${p.valueAsString}' as semantic version")
        }
}
