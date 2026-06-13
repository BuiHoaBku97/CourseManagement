package org.example.entity;

import java.util.Locale;
import java.util.Objects;

public final class CourseTopicSpec {
    private final String topicCode;
    private final int level;

    public CourseTopicSpec(String topicCode, int level) {
        this.topicCode = normalizeTopicCode(topicCode);
        if (level <= 0) {
            throw new IllegalArgumentException("Level phai lon hon 0.");
        }
        this.level = level;
    }

    public String getTopicCode() {
        return topicCode;
    }

    public int getLevel() {
        return level;
    }

    public String toDisplayText() {
        return topicCode + "_" + level;
    }

    @Override
    public String toString() {
        return toDisplayText();
    }

    private String normalizeTopicCode(String value) {
        String normalized = Objects.requireNonNull(value, "topicCode").trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Topic code khong duoc de trong.");
        }
        return normalized.toUpperCase(Locale.ROOT);
    }
}
