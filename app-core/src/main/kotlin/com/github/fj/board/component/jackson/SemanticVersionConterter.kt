/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.github.fj.lib.util.ProtectedProperty
import de.skuzzle.semantic.Version
import java.io.IOException

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
class SemanticVersionJacksonSerialiser : StdSerializer<Version>(Version::class.java) {
    override fun serialize(value: Version, gen: JsonGenerator, serializers: SerializerProvider) {
        serializers.defaultSerializeValue(value.toString(), gen)
    }
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Jul - 2020
 */
class SemanticVersionJacksonDeserialiser : JsonDeserializer<Version>(), ContextualDeserializer {
    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty): JsonDeserializer<*> =
        SemanticVersionJacksonDeserialiser()

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Version =
        Version.parseVersion(p.valueAsString)
}
