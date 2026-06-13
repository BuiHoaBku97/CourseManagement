package org.example.entity;

import java.util.Objects;

public final class CourseRecommendation {
    private final Course course;
    private final boolean recommended;
    private final int recommendationGap;

    public CourseRecommendation(Course course, boolean recommended, int recommendationGap) {
        this.course = Objects.requireNonNull(course, "course");
        this.recommended = recommended;
        this.recommendationGap = recommendationGap;
    }

    public Course getCourse() {
        return course;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public int getRecommendationGap() {
        return recommendationGap;
    }

    public String getLabel() {
        return recommended ? "DE XUAT" : "";
    }
}
