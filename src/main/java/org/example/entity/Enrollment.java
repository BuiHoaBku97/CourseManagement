package org.example.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Enrollment {
    private final int id;
    private final int studentId;
    private final int courseId;
    private final LocalDateTime registeredAt;
    private final EnrollmentStatus status;

    public Enrollment(
            int id, int studentId, int courseId, LocalDateTime registeredAt, EnrollmentStatus status) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.registeredAt = Objects.requireNonNull(registeredAt, "registeredAt");
        this.status = Objects.requireNonNull(status, "status");
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }
}
