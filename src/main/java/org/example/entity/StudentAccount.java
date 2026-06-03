package org.example.entity;

import java.util.Objects;

public final class StudentAccount {
    private final int id;
    private final String name;
    private final String email;
    private final String phone;
    private final String passwordHash;

    public StudentAccount(int id, String name, String email, String phone, String passwordHash) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "name").trim();
        this.email = Objects.requireNonNull(email, "email").trim();
        this.phone = phone == null ? null : phone.trim();
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash").trim();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
}
