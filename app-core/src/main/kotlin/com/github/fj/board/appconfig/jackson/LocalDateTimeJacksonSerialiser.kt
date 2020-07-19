/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.appconfig.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Truncate all LocalDateTime values below seconds, to avoid time-precision problems.
 * We don't need such accuracy for negotiations, since this application is not a time critical one.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 */
class LocalDateTimeJacksonSerialiser : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {
    override fun serialize(value: LocalDateTime, gen: JsonGenerator, serializers: SerializerProvider) {
        serializers.defaultSerializeValue(value.truncatedTo(ChronoUnit.SECONDS).toString(), gen)
    }
}
