/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.post.dto;

import com.github.fj.board.endpoint.v1.post.request.DeleteAttachmentRequest;

import java.util.UUID;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
public final class DeleteAttachmentRequestBuilder {
    private String accessId = "";

    public DeleteAttachmentRequestBuilder() {
    }

    public DeleteAttachmentRequestBuilder(final DeleteAttachmentRequest src) {
        this.accessId = src.getAccessId();
    }

    public DeleteAttachmentRequestBuilder accessId(final String value) {
        this.accessId = value;
        return this;
    }

    public DeleteAttachmentRequest build() {
        return new DeleteAttachmentRequest(
                /* accessId */ accessId
        );
    }

    public static DeleteAttachmentRequest createRandom() {
        return new DeleteAttachmentRequestBuilder()
                .accessId(UUID.randomUUID().toString())
                .build();
    }
}
