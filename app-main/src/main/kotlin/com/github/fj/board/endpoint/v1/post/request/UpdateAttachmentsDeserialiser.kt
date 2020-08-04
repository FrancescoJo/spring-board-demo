/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package com.github.fj.board.endpoint.v1.post.request

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.github.fj.board.endpoint.v1.post.request.AttachmentModeRequest.UNDEFINED
import com.github.fj.lib.annotation.VisibleForTesting
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
class UpdateAttachmentsDeserialiser : StdDeserializer<UpdateAttachmentRequest>(
    UpdateAttachmentsDeserialiser::class.java
), InitializingBean {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): UpdateAttachmentRequest {
        val node: JsonNode = p.codec.readTree(p)

        val modeStr = node.get("mode").textValue()
        val payloadNode = node.get("payload")

        val mode = AttachmentModeRequest.byKey(modeStr).also {
            if (it == UNDEFINED) {
                throw JsonParseException(p, "Cannot determine `mode` for input string '$modeStr'")
            }
        }

        val payload = try {
            getObjMapper().treeToValue(payloadNode, mode.expectedRequestClass.java).also {
                if (it == null) {
                    throw JsonParseException(p, "Cannot parse `payload` for input string '$payloadNode'")
                }
            }
        } catch (ex: JsonProcessingException) {
            throw JsonParseException(p, "Cannot parse `payload` for input string '$payloadNode'", ex)
        }
        return UpdateAttachmentRequest(mode, payload)
    }

    private fun getObjMapper() = requireNotNull(objMapper)

    override fun afterPropertiesSet() {
        if (objMapper == null) {
            LOG.warn(
                "No system-wide Jackson object mapper is configured. Using default ObjectMapper instance. " +
                        "However, this may cause an unexpected deserialisation failure(s)."
            )
            objMapper = ObjectMapper()
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(UpdateAttachmentsDeserialiser::class.java)

        @VisibleForTesting
        var objMapper: ObjectMapper? = null

        fun injectMapper(objMapper: ObjectMapper) {
            if (this.objMapper == null) {
                this.objMapper = objMapper
                LOG.trace("Jackson object mapper instance($objMapper) has been set")
            } else {
                LOG.warn("Injection of a new Jackson object mapper instance($objMapper) is ignored.")
            }
        }
    }
}
