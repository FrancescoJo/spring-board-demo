/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.v1

import com.github.fj.board.endpoint.v1.reply.response.ReplyInfoResponse
import com.github.fj.board.persistence.repository.reply.ReplyRepository
import com.github.fj.board.vo.board.BoardInfo
import com.github.fj.board.vo.post.PostDetailedInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import testcase.common.CreatedUser

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
class ReplyTestBase extends PostTestBase {
    @Autowired
    private ReplyRepository replyRepo

    protected CreatedUser self
    protected BoardInfo currentBoard
    protected PostDetailedInfo currentPost

    def setup() {
        this.self = createRandomUser()
        this.currentBoard = createRandomBoardOf(self)
        this.currentPost = createRandomPostOf(self, currentBoard)
    }

    def cleanup() {
        replyRepo.deleteAll()
    }

    protected static ResponseFieldsSnippet replyInfoResponseFieldsDoc() {
        final List<FieldDescriptor> fields = [
                fieldWithPath("body.postId")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_POST_ID),
                fieldWithPath("body.replyId")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_REPLY_ID),
                fieldWithPath("body.writerNickname")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_WRITER_NICKNAME),
                fieldWithPath("body.writerLoginName")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_WRITER_LOGIN_NAME),
                fieldWithPath("body.lastModifiedDate")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_LAST_MODIFIED_DATE),
                fieldWithPath("body.lastModifiedIp")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_LAST_MODIFIED_IP),
                fieldWithPath("body.lastModifiedPlatformType")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_LAST_MODIFIED_PLATFORM_TYPE),
                fieldWithPath("body.edited")
                        .type(JsonFieldType.BOOLEAN)
                        .description(ReplyInfoResponse.DESC_EDITED),
                fieldWithPath("body.number")
                        .type(JsonFieldType.NUMBER)
                        .description(ReplyInfoResponse.DESC_NUMBER),
                fieldWithPath("body.contents")
                        .type(JsonFieldType.STRING)
                        .description(ReplyInfoResponse.DESC_CONTENTS)
        ]

        return responseFields(basicFieldsDoc() + fields)
    }
}
