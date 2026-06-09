package org.example.entity;

import java.time.LocalDate;
import java.util.Objects;

public final class Course {
    private final int id;
    private final String name;
    private final int duration;
    private final String instructor;
    private final LocalDate createdAt;

    public Course(int id, String name, int duration, String instructor, LocalDate createdAt) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name").trim();
        this.duration = duration;
        this.instructor = Objects.requireNonNull(instructor, "instructor").trim();
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getInstructor() {
        return instructor;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
