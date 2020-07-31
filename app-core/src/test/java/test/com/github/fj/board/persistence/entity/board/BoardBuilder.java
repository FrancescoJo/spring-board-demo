/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.persistence.entity.board;

import com.github.fj.board.persistence.entity.board.Board;
import com.github.fj.board.persistence.entity.user.User;
import com.github.fj.board.persistence.model.board.BoardAccess;
import com.github.fj.board.persistence.model.board.BoardMode;
import com.github.fj.board.persistence.model.board.BoardStatus;
import com.github.fj.lib.time.DateTimeUtilsKt;
import com.github.fj.lib.util.UuidExtensions;
import test.com.github.fj.board.persistence.entity.user.UserBuilder;

import javax.annotation.Nonnull;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.fj.lib.time.DateTimeUtilsKt.utcNow;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomAlphaNumericString;
import static com.github.fj.lib.util.RandomUtilsKt.getRandomPositiveLong;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Jul - 2020
 */
public class BoardBuilder {
    private long id = 0;
    private UUID accessId = UuidExtensions.INSTANCE.getEMPTY_UUID();
    private BoardStatus status = BoardStatus.NORMAL;
    private BoardAccess access = BoardAccess.PUBLIC;
    private BoardMode mode = BoardMode.FREE_STYLE;
    private String key = "";
    private String name = "";
    private String description = "";
    private LocalDateTime createdDate = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private LocalDateTime modifiedDate = DateTimeUtilsKt.getLOCAL_DATE_TIME_MIN();
    private User creator = null;

    public BoardBuilder() {
    }

    public BoardBuilder(final @Nonnull Board src) {
        this.id = src.getId();
        this.accessId = src.getAccessId();
        this.status = src.getStatus();
        this.key = src.getKey();
        this.name = src.getName();
        this.description = src.getDescription();
        this.createdDate = src.getCreatedDate();
        this.modifiedDate = src.getModifiedDate();
        this.creator = src.getCreator();
    }

    public BoardBuilder id(final long value) {
        this.id = value;
        return this;
    }

    public BoardBuilder accessId(final @Nonnull UUID value) {
        this.accessId = value;
        return this;
    }

    public BoardBuilder status(final @Nonnull BoardStatus value) {
        this.status = value;
        return this;
    }

    public BoardBuilder access(final @Nonnull BoardAccess value) {
        this.access = value;
        return this;
    }

    public BoardBuilder mode(final @Nonnull BoardMode value) {
        this.mode = value;
        return this;
    }

    public BoardBuilder key(final @Nonnull String value) {
        this.key = value;
        return this;
    }

    public BoardBuilder name(final @Nonnull String value) {
        this.name = value;
        return this;
    }

    public BoardBuilder description(final @Nonnull String value) {
        this.description = value;
        return this;
    }

    public BoardBuilder createdDate(final @Nonnull LocalDateTime value) {
        this.createdDate = value;
        return this;
    }

    public BoardBuilder modifiedDate(final @Nonnull LocalDateTime value) {
        this.modifiedDate = value;
        return this;
    }

    public BoardBuilder creator(final @Nonnull User value) {
        this.creator = value;
        return this;
    }

    public Board build() {
        final Board object = new Board();

        object.setId(id);
        object.setAccessId(accessId);
        object.setStatus(status);
        object.setAccess(access);
        object.setMode(mode);
        object.setKey(key);
        object.setName(name);
        object.setDescription(description);
        object.setCreatedDate(createdDate);
        object.setModifiedDate(modifiedDate);
        object.setCreator(creator);

        return object;
    }

    public static Board createRandom() throws UnknownHostException {
        final LocalDateTime now = utcNow();

        return new BoardBuilder()
                .id(getRandomPositiveLong(1L, Long.MAX_VALUE))
                .accessId(UUID.randomUUID())
                .status(BoardStatus.NORMAL)
                .access(BoardAccess.PUBLIC)
                .mode(BoardMode.FREE_STYLE)
                .key(getRandomAlphaNumericString(8))
                .name("name: " + getRandomAlphaNumericString(12))
                .description("desc: " + getRandomAlphaNumericString(24))
                .createdDate(now)
                .modifiedDate(now)
                .creator(UserBuilder.createRandom())
                .build();
    }
}
