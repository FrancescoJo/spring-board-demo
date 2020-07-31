/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.post.dto;

import com.github.fj.board.endpoint.v1.post.request.UpdateAttachmentRequest;
import com.github.fj.board.endpoint.v1.post.request.UpdatePostRequest;
import com.github.fj.board.persistence.model.post.PostMode;
import com.github.fj.lib.collection.CollectionUtilsKt;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomPositiveInt;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
public final class UpdatePostRequestBuilder {
    private String accessId = "";
    private PostMode mode = PostMode.FREE_REPLY;
    private String title = "";
    private String content = "";
    private List<UpdateAttachmentRequest> attachments = Collections.emptyList();

    public UpdatePostRequestBuilder() {
    }

    public UpdatePostRequestBuilder(final UpdatePostRequest src) {
        this.accessId = src.getAccessId();
        this.title = src.getTitle();
        this.content = src.getContent();
        this.attachments = src.getAttachments();
    }

    public UpdatePostRequestBuilder accessId(final String value) {
        this.accessId = value;
        return this;
    }

    public UpdatePostRequestBuilder mode(final PostMode value) {
        this.mode = value;
        return this;
    }

    public UpdatePostRequestBuilder title(final String value) {
        this.title = value;
        return this;
    }

    public UpdatePostRequestBuilder content(final String value) {
        this.content = value;
        return this;
    }

    public UpdatePostRequestBuilder attachments(final List<UpdateAttachmentRequest> value) {
        this.attachments = value;
        return this;
    }

    public UpdatePostRequest build() {
        return new UpdatePostRequest(
                /* accessId */    accessId,
                /* mode */        mode,
                /* title */       title,
                /* content */     content,
                /* attachments */ attachments
        );
    }

    public static UpdatePostRequest createRandom() {
        return new UpdatePostRequestBuilder()
                .accessId(UUID.randomUUID().toString())
                .mode(randomEnumConst(PostMode.class))
                .title(getRandomAlphaNumericString(16))
                .content(getRandomAlphaNumericString(255))
                .attachments(CollectionUtilsKt.iterationsOf(
                        getRandomPositiveInt(1, 4), UpdateAttachmentRequestBuilder::createRandom
                ))
                .build();
    }
}
