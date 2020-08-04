/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.post.dto;

import com.github.fj.board.endpoint.v1.post.request.AttachmentModeRequest;
import com.github.fj.board.endpoint.v1.post.request.UpdateAttachmentRequest;
import com.github.fj.lib.collection.CollectionUtilsKt;

import java.util.List;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomPositiveInt;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 30 - Jul - 2020
 */
public final class UpdateAttachmentRequestBuilder {
    private AttachmentModeRequest mode = AttachmentModeRequest.UNDEFINED;
    private Object payload = null;

    public UpdateAttachmentRequestBuilder() {
    }

    public UpdateAttachmentRequestBuilder(final UpdateAttachmentRequest src) {
        this.mode = src.getMode();
        this.payload = src.getPayload();
    }

    public UpdateAttachmentRequestBuilder mode(final AttachmentModeRequest value) {
        this.mode = value;
        return this;
    }

    public UpdateAttachmentRequestBuilder payload(final Object value) {
        this.payload = value;
        return this;
    }

    public UpdateAttachmentRequest build() {
        return new UpdateAttachmentRequest(
                /* mode */    mode,
                /* payload */ payload
        );
    }

    public static List<UpdateAttachmentRequest> createRandomBulk(final AttachmentModeRequest mode) {
        return CollectionUtilsKt.iterationsOf(
                getRandomPositiveInt(1, 4), () -> createRandom(mode)
        );
    }

    public static UpdateAttachmentRequest createRandom() {
        return createRandom(randomEnumConst(AttachmentModeRequest.class));
    }

    public static UpdateAttachmentRequest createRandom(final AttachmentModeRequest mode) {
        final Object payload;

        switch (mode) {
            case CREATE:
                payload = CreateAttachmentRequestBuilder.createRandom();
                break;
            case DELETE:
                payload = DeleteAttachmentRequestBuilder.createRandom();
                break;
            default:
                throw new UnsupportedOperationException("No payload generation strategy found for " + mode);
        }

        return new UpdateAttachmentRequestBuilder()
                .mode(mode)
                .payload(payload)
                .build();
    }
}
