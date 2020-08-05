/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.reply.dto;

import com.github.fj.board.endpoint.v1.reply.request.CreateReplyRequest;

import javax.annotation.Nonnull;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 05 - Aug - 2020
 */
public final class CreateReplyRequestBuilder {
    private String content = "";

    public CreateReplyRequestBuilder() {
    }

    public CreateReplyRequestBuilder(final CreateReplyRequest src) {
        this.content = src.getContent();
    }

    public CreateReplyRequestBuilder content(final @Nonnull String value) {
        this.content = value;
        return this;
    }

    public CreateReplyRequest build() {
        return new CreateReplyRequest(
                /* content */ content
        );
    }

    public static CreateReplyRequest createRandom() {
        return new CreateReplyRequestBuilder()
                .content(getRandomAlphaNumericString(255))
                .build();
    }
}
