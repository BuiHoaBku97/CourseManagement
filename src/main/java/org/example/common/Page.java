package org.example.common;

import java.util.List;
import java.util.Objects;

public final class Page<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;

    public Page(List<T> content, int page, int size, long totalElements) {
        this.content = List.copyOf(Objects.requireNonNull(content, "content"));
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int totalPages() {
        if (totalElements == 0) {
            return 1;
        }
        return (int) Math.ceil((double) totalElements / size);
    }

    public boolean hasNext() {
        return page < totalPages();
    }

    public boolean hasPrevious() {
        return page > 1;
    }
}
