/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.reply.dto;

import com.github.fj.board.endpoint.v1.reply.request.UpdateReplyRequest;

import javax.annotation.Nonnull;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 06 - Aug - 2020
 */
public final class UpdateReplyRequestBuilder {
    private String content = "";

    public UpdateReplyRequestBuilder() {
    }

    public UpdateReplyRequestBuilder(final UpdateReplyRequest src) {
        this.content = src.getContent();
    }

    public UpdateReplyRequestBuilder content(final @Nonnull String value) {
        this.content = value;
        return this;
    }

    public UpdateReplyRequest build() {
        return new UpdateReplyRequest(
                /* content */ content
        );
    }

    public static UpdateReplyRequest createRandom() {
        return new UpdateReplyRequestBuilder()
                .content(getRandomAlphaNumericString(255))
                .build();
    }
}
