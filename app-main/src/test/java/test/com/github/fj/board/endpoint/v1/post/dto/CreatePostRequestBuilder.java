/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.endpoint.v1.post.dto;

import com.github.fj.board.endpoint.v1.post.request.CreateAttachmentRequest;
import com.github.fj.board.endpoint.v1.post.request.CreatePostRequest;
import com.github.fj.board.persistence.model.post.PostMode;
import com.github.fj.lib.collection.CollectionUtilsKt;

import java.util.Collections;
import java.util.List;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomPositiveInt;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Jul - 2020
 */
public final class CreatePostRequestBuilder {
    private PostMode mode = PostMode.FREE_REPLY;
    private String title = "";
    private String content = "";
    private List<CreateAttachmentRequest> attachments = Collections.emptyList();

    public CreatePostRequestBuilder() {
    }

    public CreatePostRequestBuilder(final CreatePostRequest src) {
        this.title = src.getTitle();
        this.content = src.getContents();
        this.attachments = src.getAttachments();
    }

    public CreatePostRequestBuilder mode(final PostMode value) {
        this.mode = value;
        return this;
    }

    public CreatePostRequestBuilder title(final String value) {
        this.title = value;
        return this;
    }

    public CreatePostRequestBuilder content(final String value) {
        this.content = value;
        return this;
    }

    public CreatePostRequestBuilder attachments(final List<CreateAttachmentRequest> value) {
        this.attachments = value;
        return this;
    }

    public CreatePostRequest build() {
        return new CreatePostRequest(
                /* mode */        this.mode,
                /* title */       this.title,
                /* content */     this.content,
                /* attachments */ this.attachments
        );
    }

    public static CreatePostRequest createRandom() {
        return new CreatePostRequestBuilder()
                .mode(PostMode.FREE_REPLY)
                .title(getRandomAlphaNumericString(16))
                .content(getRandomAlphaNumericString(512))
                .attachments(CollectionUtilsKt.iterationsOf(
                        getRandomPositiveInt(1, 4), CreateAttachmentRequestBuilder::createRandom
                ))
                .build();
    }
}
