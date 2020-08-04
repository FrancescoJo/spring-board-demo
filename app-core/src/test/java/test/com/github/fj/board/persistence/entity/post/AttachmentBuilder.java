/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.persistence.entity.post;

import com.github.fj.board.persistence.entity.post.Attachment;
import com.github.fj.board.persistence.entity.post.Post;
import com.github.fj.board.persistence.model.post.ContentStatus;
import com.github.fj.lib.util.UuidExtensions;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomPositiveLong;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomMimeType;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomUri;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
public final class AttachmentBuilder {
    private final Post parentPost;

    private long id = 0L;
    private UUID accessId = UuidExtensions.INSTANCE.getEMPTY_UUID();
    private ContentStatus status = ContentStatus.NOT_REVIEWED;
    private String name = "";
    private String uri = "";
    private String mimeType = "";

    public AttachmentBuilder(final @Nonnull Attachment src) {
        this.parentPost = src.getPost();

        this.id = src.getId();
        this.accessId = src.getAccessId();
        this.status = src.getStatus();
        this.name = src.getName();
        this.uri = src.getUri();
        this.mimeType = src.getMimeType();
    }

    public AttachmentBuilder(final @Nonnull Post parentPost) {
        this.parentPost = parentPost;
    }

    public AttachmentBuilder id(final long value) {
        this.id = value;
        return this;
    }

    public AttachmentBuilder accessId(final UUID value) {
        this.accessId = value;
        return this;
    }

    public AttachmentBuilder status(final ContentStatus value) {
        this.status = value;
        return this;
    }

    public AttachmentBuilder name(final String value) {
        this.name = value;
        return this;
    }

    public AttachmentBuilder uri(final String value) {
        this.uri = value;
        return this;
    }

    public AttachmentBuilder mimeType(final String value) {
        this.mimeType = value;
        return this;
    }

    public Attachment build() {
        final Attachment object = new Attachment();

        object.setPost(parentPost);

        object.setId(id);
        object.setAccessId(accessId);
        object.setStatus(status);
        object.setName(name);
        object.setUri(uri);
        object.setMimeType(mimeType);

        return object;
    }

    public static Attachment createRandomOf(final @Nonnull Post parentPost) {
        return new AttachmentBuilder(parentPost)
                .id(getRandomPositiveLong(1L, Long.MAX_VALUE))
                .accessId(UUID.randomUUID())
                .status(ContentStatus.NOT_REVIEWED)
                .name(getRandomAlphaNumericString(8))
                .uri(randomUri())
                .mimeType(randomMimeType())
                .build();
    }
}
