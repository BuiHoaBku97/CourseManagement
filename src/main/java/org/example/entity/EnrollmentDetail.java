package org.example.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public final class EnrollmentDetail {
    private final int id;
    private final int courseId;
    private final String courseName;
    private final int studentId;
    private final String studentName;
    private final EnrollmentStatus status;
    private final LocalDateTime registeredAt;

    public EnrollmentDetail(
            int id,
            int courseId,
            String courseName,
            int studentId,
            String studentName,
            EnrollmentStatus status,
            LocalDateTime registeredAt) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = Objects.requireNonNull(courseName, "courseName").trim();
        this.studentId = studentId;
        this.studentName = Objects.requireNonNull(studentName, "studentName").trim();
        this.status = Objects.requireNonNull(status, "status");
        this.registeredAt = Objects.requireNonNull(registeredAt, "registeredAt");
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }
}
