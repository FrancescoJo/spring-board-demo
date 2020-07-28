/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.post.dto;

import com.github.fj.board.endpoint.v1.post.request.CreateAttachmentRequest;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomMimeType;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomUri;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
public final class CreateAttachmentRequestBuilder {
    private String uri = "";
    private String mimeType = "";
    private String name = "";

    public CreateAttachmentRequestBuilder() {
    }

    public CreateAttachmentRequestBuilder(final CreateAttachmentRequest src) {
        this.uri = src.getUri();
        this.mimeType = src.getMimeType();
        this.name = src.getName();
    }

    public CreateAttachmentRequestBuilder uri(final String value) {
        this.uri = value;
        return this;
    }

    public CreateAttachmentRequestBuilder mimeType(final String value) {
        this.mimeType = value;
        return this;
    }

    public CreateAttachmentRequestBuilder name(final String value) {
        this.name = value;
        return this;
    }

    public CreateAttachmentRequest build() {
        return new CreateAttachmentRequest(
                /* uri */      this.uri,
                /* mimeType */ this.mimeType,
                /* name */     this.name
        );
    }

    public static CreateAttachmentRequest createRandom() {
        return new CreateAttachmentRequestBuilder()
                .uri(randomUri())
                .mimeType(randomMimeType())
                .name(getRandomAlphaNumericString(8))
                .build();
    }
}
