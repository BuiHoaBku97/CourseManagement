package org.example.utils;

import java.util.regex.Pattern;

public final class InputValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9_.]+@gmail\\.com$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    private static final Pattern COURSE_TOPIC_SPEC_PATTERN = Pattern.compile("^[A-Za-z0-9]+_[1-9][0-9]*$");

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

    public static boolean isValidEmail(String value) {
        if (isBlank(value)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(value.trim()).matches();
    }

    public static boolean isValidPhone(String value) {
        if (isBlank(value)) {
            return false;
        }
        return PHONE_PATTERN.matcher(value.trim()).matches();
    }

    public static boolean isValidPhoneOrBlank(String value) {
        return isBlank(value) || isValidPhone(value);
    }

    public static boolean isValidCourseTopicSpec(String value) {
        if (isBlank(value)) {
            return false;
        }
        return COURSE_TOPIC_SPEC_PATTERN.matcher(value.trim()).matches();
    }
}
