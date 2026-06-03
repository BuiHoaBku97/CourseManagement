package org.example.utils;

public final class InputValidator {
    private InputValidator() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static Integer parseInteger(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static boolean isInRange(int value, int minInclusive, int maxInclusive) {
        return value >= minInclusive && value <= maxInclusive;
    }
}
