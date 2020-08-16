/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package test.com.github.fj.board.vo;

import com.github.fj.board.vo.PagedData;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Aug - 2020
 */
public class MockPagedData<T> implements PagedData<T> {
    public final int page;
    public final int size;
    public final long totalCount;
    public final List<T> data;

    public MockPagedData(final int page, final int size, final long totalCount, final @Nonnull List<T> data) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.data = data;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public long getTotalCount() {
        return totalCount;
    }

    @Nonnull
    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "MockPageable{" +
                "page=" + page +
                ", size= " + size +
                ", totalCount=" + totalCount +
                ", data=" + data +
                "}";
    }
}
