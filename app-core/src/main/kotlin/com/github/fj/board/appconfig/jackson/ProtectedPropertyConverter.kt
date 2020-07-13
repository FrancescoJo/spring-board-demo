/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.github.fj.lib.util.ProtectedProperty
import java.io.IOException

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Nov - 2018
 */
internal class ProtectedPropertyJacksonSerialiser
    : StdSerializer<ProtectedProperty<*>>(ProtectedProperty::class.java) {
    override fun serialize(value: ProtectedProperty<*>, gen: JsonGenerator, serializers: SerializerProvider) {
        serializers.defaultSerializeValue(value.value, gen)
    }
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Nov - 2018
 */
internal class ProtectedPropertyJacksonDeserialiser
    : JsonDeserializer<ProtectedProperty<*>>(), ContextualDeserializer {
    private lateinit var valueType: JavaType

    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty): JsonDeserializer<*> {
        return ProtectedPropertyJacksonDeserialiser().apply {
            this.valueType = property.type.containedType(0)
        }
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ProtectedProperty<*> {
        return try {
            ProtectedProperty<Any>(ctxt.readValue(p, valueType))
        } catch (e: IOException) {
            throw UnsupportedOperationException("Unable to parse wrapped $valueType as JSON.", e)
        }
    }
}
