package org.example.entity;

public final class SystemSummary {
    private final long courseCount;
    private final long studentCount;

    public SystemSummary(long courseCount, long studentCount) {
        this.courseCount = courseCount;
        this.studentCount = studentCount;
    }

    public long getCourseCount() {
        return courseCount;
    }

    public long getStudentCount() {
        return studentCount;
    }
}
