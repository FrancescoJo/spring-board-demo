/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1

import com.github.fj.board.endpoint.v1.post.dto.CreateAttachmentRequest
import com.github.fj.board.endpoint.v1.post.dto.CreatePostRequest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.RequestFieldsSnippet

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
class PostTestBase extends BoardTestBase {
    static RequestFieldsSnippet createRequestFieldsDoc() {
        return requestFields(
                fieldWithPath("mode")
                        .type(JsonFieldType.STRING)
                        .description(CreatePostRequest.DESC_MODE),
                fieldWithPath("title")
                        .type(JsonFieldType.STRING)
                        .description(CreatePostRequest.DESC_TITLE),
                fieldWithPath("content")
                        .type(JsonFieldType.STRING)
                        .description(CreatePostRequest.DESC_CONTENT),
                fieldWithPath("attachments[]")
                        .type(JsonFieldType.ARRAY)
                        .description(CreatePostRequest.DESC_CONTENT),
                fieldWithPath("attachments[].uri")
                        .type(JsonFieldType.STRING)
                        .description(CreateAttachmentRequest.DESC_URI),
                fieldWithPath("attachments[].mimeType")
                        .type(JsonFieldType.STRING)
                        .description(CreateAttachmentRequest.DESC_MIME_TYPE),
                fieldWithPath("attachments[].name")
                        .type(JsonFieldType.STRING)
                        .description(CreateAttachmentRequest.DESC_NAME)
        )
    }
}
