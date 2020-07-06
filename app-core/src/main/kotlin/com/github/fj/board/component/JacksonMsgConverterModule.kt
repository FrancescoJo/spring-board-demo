/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.component

import com.fasterxml.jackson.databind.module.SimpleModule
import com.github.fj.board.component.jackson.ProtectedPropertyJacksonDeserialiser
import com.github.fj.board.component.jackson.ProtectedPropertyJacksonSerialiser
import com.github.fj.board.component.jackson.SemanticVersionJacksonDeserialiser
import com.github.fj.board.component.jackson.SemanticVersionJacksonSerialiser
import com.github.fj.lib.util.ProtectedProperty
import de.skuzzle.semantic.Version

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 08 - Nov - 2018
 */
class JacksonMsgConverterModule : SimpleModule(JacksonMsgConverterModule::class.java.canonicalName) {
    init {
        addSerializer(ProtectedPropertyJacksonSerialiser())
        addSerializer(SemanticVersionJacksonSerialiser())

        addDeserializer(ProtectedProperty::class.java, ProtectedPropertyJacksonDeserialiser())
        addDeserializer(Version::class.java, SemanticVersionJacksonDeserialiser())
    }
}
