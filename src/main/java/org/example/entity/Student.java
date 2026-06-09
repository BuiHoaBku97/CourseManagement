package org.example.entity;

import java.time.LocalDate;
import java.util.Objects;

public final class Student {
    private final int id;
    private final String name;
    private final LocalDate dob;
    private final boolean sex;
    private final String email;
    private final String phone;
    private final String passwordHash;
    private final LocalDate createdAt;

    public Student(
            int id,
            String name,
            LocalDate dob,
            boolean sex,
            String email,
            String phone,
            String passwordHash,
            LocalDate createdAt) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name").trim();
        this.dob = Objects.requireNonNull(dob, "dob");
        this.sex = sex;
        this.email = Objects.requireNonNull(email, "email").trim();
        this.phone = phone == null ? null : phone.trim();
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash").trim();
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public boolean isSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
