/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.persistence.entity.post;

import com.github.fj.board.persistence.entity.board.Board;
import com.github.fj.board.persistence.entity.post.Attachment;
import com.github.fj.board.persistence.entity.post.Post;
import com.github.fj.board.persistence.entity.user.User;
import com.github.fj.board.persistence.model.auth.PlatformType;
import com.github.fj.board.persistence.model.post.ContentStatus;
import com.github.fj.board.persistence.model.post.PostMode;
import com.github.fj.lib.collection.CollectionUtilsKt;
import com.github.fj.lib.net.InetAddressExtensions;
import com.github.fj.lib.time.DateTimeUtilsKt;
import com.github.fj.lib.util.UuidExtensions;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.fj.lib.time.DateTimeUtilsKt.utcNow;
import static com.github.fj.lib.util.RandomUtilsKt.*;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Jul - 2020
 */
public final class PostBuilder {
    private final Board board;
    private final User user;

    private long id = 0L;
    private UUID accessId = UuidExtensions.INSTANCE.getEMPTY_UUID();
    private ContentStatus status = ContentStatus.NOT_REVIEWED;
    private PostMode mode = PostMode.FREE_REPLY;
    private LocalDateTime lastModifiedDate = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private InetAddress lastModifiedIp = InetAddressExtensions.INSTANCE.getEMPTY_INET_ADDRESS();
    private PlatformType lastModifiedPlatformType = PlatformType.UNDEFINED;
    private boolean edited = false;
    private long number = 0L;
    private String title = "";
    private String contents = "";
    private long viewedCount = 0L;
    private List<Attachment> attachments = Collections.emptyList();

    public PostBuilder(final @Nonnull Post src) {
        this.board = src.getBoard();
        this.user = src.getCreator();

        this.id = src.getId();
        this.accessId = src.getAccessId();
        this.status = src.getStatus();
        this.mode = src.getMode();
        this.lastModifiedDate = src.getLastModifiedDate();
        this.lastModifiedIp = src.getLastModifiedIp();
        this.lastModifiedPlatformType = src.getLastModifiedPlatformType();
        this.edited = src.getEdited();
        this.number = src.getNumber();
        this.title = src.getTitle();
        this.contents = src.getContents();
        this.viewedCount = src.getViewedCount();
        this.attachments = src.getAttachments();
    }

    public PostBuilder(final @Nonnull Board parentBoard, final @Nonnull User creator) {
        this.board = parentBoard;
        this.user = creator;
    }

    public PostBuilder id(final long value) {
        this.id = value;
        return this;
    }

    public PostBuilder accessId(final @Nonnull UUID value) {
        this.accessId = value;
        return this;
    }

    public PostBuilder status(final @Nonnull ContentStatus value) {
        this.status = value;
        return this;
    }

    public PostBuilder mode(final @Nonnull PostMode value) {
        this.mode = value;
        return this;
    }

    public PostBuilder lastModifiedDate(final @Nonnull LocalDateTime value) {
        this.lastModifiedDate = value;
        return this;
    }

    public PostBuilder lastModifiedIp(final @Nonnull InetAddress value) {
        this.lastModifiedIp = value;
        return this;
    }

    public PostBuilder lastModifiedPlatformType(final @Nonnull PlatformType value) {
        this.lastModifiedPlatformType = value;
        return this;
    }

    public PostBuilder edited(final boolean value) {
        this.edited = value;
        return this;
    }

    public PostBuilder number(final long value) {
        this.number = value;
        return this;
    }

    public PostBuilder title(final @Nonnull String value) {
        this.title = value;
        return this;
    }

    public PostBuilder contents(final @Nonnull String value) {
        this.contents = value;
        return this;
    }

    public PostBuilder viewedCount(final long value) {
        this.viewedCount = value;
        return this;
    }

    public PostBuilder attachments(final @Nonnull List<Attachment> value) {
        this.attachments = value;
        return this;
    }

    public Post build() {
        final Post object = new Post();

        object.setBoard(board);
        object.setCreator(user);

        object.setId(id);
        object.setAccessId(accessId);
        object.setStatus(status);
        object.setMode(mode);
        object.setLastModifiedDate(lastModifiedDate);
        object.setLastModifiedIp(lastModifiedIp);
        object.setLastModifiedPlatformType(lastModifiedPlatformType);
        object.setEdited(edited);
        object.setNumber(number);
        object.setTitle(title);
        object.setContents(contents);
        object.setViewedCount(viewedCount);
        object.setAttachments(attachments);

        return object;
    }

    public static Post createRandomOf(
            final @Nonnull Board parentBoard,
            final @Nonnull User creator
    ) throws UnknownHostException {
        final LocalDateTime now = utcNow();
        final InetAddress localhost = InetAddress.getLocalHost();

        final Post post = new PostBuilder(parentBoard, creator)
                .id(getRandomPositiveLong(1L, Long.MAX_VALUE))
                .accessId(UUID.randomUUID())
                .status(ContentStatus.NOT_REVIEWED)
                .mode(PostMode.FREE_REPLY)
                .lastModifiedDate(now)
                .lastModifiedIp(localhost)
                .lastModifiedPlatformType(randomEnumConst(PlatformType.class))
                .edited(false)
                .number(getRandomPositiveLong(1L, Long.MAX_VALUE))
                .title(getRandomAlphaNumericString(16))
                .contents(getRandomAlphaNumericString(255))
                .viewedCount(0L)
                .build();

        final List<Attachment> attachments = CollectionUtilsKt.iterationsOf(
                getRandomPositiveInt(1, 4), () -> AttachmentBuilder.createRandomOf(post)
        );

        return new PostBuilder(post)
                .attachments(attachments)
                .build();
    }
}
