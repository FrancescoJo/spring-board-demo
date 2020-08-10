/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.persistence.entity.reply;

import com.github.fj.board.persistence.entity.board.Board;
import com.github.fj.board.persistence.entity.post.Post;
import com.github.fj.board.persistence.entity.reply.Reply;
import com.github.fj.board.persistence.entity.user.User;
import com.github.fj.board.persistence.model.auth.PlatformType;
import com.github.fj.board.persistence.model.post.ContentStatus;
import com.github.fj.lib.net.InetAddressExtensions;
import com.github.fj.lib.time.DateTimeUtilsKt;
import com.github.fj.lib.util.UuidExtensions;
import test.com.github.fj.board.persistence.entity.board.BoardBuilder;
import test.com.github.fj.board.persistence.entity.post.PostBuilder;
import test.com.github.fj.board.persistence.entity.user.UserBuilder;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.fj.lib.time.DateTimeUtilsKt.utcNow;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomPositiveLong;
import static test.com.github.fj.lib.util.RandomTestArgUtils.randomEnumConst;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 04 - Aug - 2020
 */
public final class ReplyBuilder {
    private final Post post;
    private final User creator;

    private long id = 0L;
    private UUID accessId = UuidExtensions.INSTANCE.getEMPTY_UUID();
    private ContentStatus status = ContentStatus.NOT_REVIEWED;
    private LocalDateTime lastModifiedDate = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private InetAddress lastModifiedIp = InetAddressExtensions.INSTANCE.getEMPTY_INET_ADDRESS();
    private PlatformType lastModifiedPlatformType = PlatformType.UNDEFINED;
    private boolean edited = false;
    private long number = 0L;
    private String contents = "";

    public ReplyBuilder(final @Nonnull Reply src) {
        this.post = src.getPost();
        this.creator = src.getCreator();
    }

    public ReplyBuilder(final @Nonnull Post parentPost, final @Nonnull User creator) {
        this.post = parentPost;
        this.creator = creator;
    }

    public ReplyBuilder id(final long value) {
        this.id = value;
        return this;
    }

    public ReplyBuilder accessId(final @Nonnull UUID value) {
        this.accessId = value;
        return this;
    }

    public ReplyBuilder status(final @Nonnull ContentStatus value) {
        this.status = value;
        return this;
    }

    public ReplyBuilder lastModifiedDate(final @Nonnull LocalDateTime value) {
        this.lastModifiedDate = value;
        return this;
    }

    public ReplyBuilder lastModifiedIp(final @Nonnull InetAddress value) {
        this.lastModifiedIp = value;
        return this;
    }

    public ReplyBuilder lastModifiedPlatformType(final @Nonnull PlatformType value) {
        this.lastModifiedPlatformType = value;
        return this;
    }

    public ReplyBuilder edited(final boolean value) {
        this.edited = value;
        return this;
    }

    public ReplyBuilder number(final long value) {
        this.number = value;
        return this;
    }

    public ReplyBuilder contents(final @Nonnull String value) {
        this.contents = value;
        return this;
    }

    public Reply build() {
        final Reply object = new Reply();

        object.setPost(post);
        object.setCreator(creator);

        object.setId(id);
        object.setAccessId(accessId);
        object.setStatus(status);
        object.setLastModifiedDate(lastModifiedDate);
        object.setLastModifiedIp(lastModifiedIp);
        object.setLastModifiedPlatformType(lastModifiedPlatformType);
        object.setEdited(edited);
        object.setNumber(number);
        object.setContents(contents);

        return object;
    }

    public static Reply createRandom() throws UnknownHostException {
        final Board board = BoardBuilder.createRandom();
        final Post post = PostBuilder.createRandomOf(board, UserBuilder.createRandom());

        return createRandomOf(post, UserBuilder.createRandom());
    }

    public static Reply createRandomOf(
            final @Nonnull Post parentPost,
            final @Nonnull User creator
    ) throws UnknownHostException {
        final LocalDateTime now = utcNow();
        final InetAddress localhost = InetAddress.getLocalHost();

        return new ReplyBuilder(parentPost, creator)
                .id(getRandomPositiveLong(1L, Long.MAX_VALUE))
                .accessId(UUID.randomUUID())
                .status(ContentStatus.NOT_REVIEWED)
                .lastModifiedDate(now)
                .lastModifiedIp(localhost)
                .lastModifiedPlatformType(randomEnumConst(PlatformType.class))
                .edited(false)
                .number(getRandomPositiveLong(1L, Long.MAX_VALUE))
                .contents(getRandomAlphaNumericString(255))
                .build();
    }
}
