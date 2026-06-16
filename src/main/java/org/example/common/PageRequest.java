package org.example.common;

public record PageRequest(int page, int size) {
    public static final int DEFAULT_SIZE = 10;

    public PageRequest {
        if (page < 1) {
            throw new IllegalArgumentException("Page must be at least 1.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be positive.");
        }
    }

    public static PageRequest firstPage() {
        return new PageRequest(1, DEFAULT_SIZE);
    }

    public int offset() {
        return (page - 1) * size;
    }

    public PageRequest next() {
        return new PageRequest(page + 1, size);
    }

    public PageRequest previous() {
        return new PageRequest(page - 1, size);
    }
}
