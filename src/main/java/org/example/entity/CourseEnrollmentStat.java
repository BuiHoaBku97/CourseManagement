package org.example.entity;

import java.util.Objects;

public final class CourseEnrollmentStat {
    private final int courseId;
    private final String courseName;
    private final long studentCount;

    public CourseEnrollmentStat(int courseId, String courseName, long studentCount) {
        this.courseId = courseId;
        this.courseName = Objects.requireNonNull(courseName, "courseName").trim();
        this.studentCount = studentCount;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public long getStudentCount() {
        return studentCount;
    }
}
