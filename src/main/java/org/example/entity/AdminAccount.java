package org.example.entity;

import java.util.Objects;

public final class AdminAccount {
    private final int id;
    private final String username;
    private final String passwordHash;

    public AdminAccount(int id, String username, String passwordHash) {
        this.id = id;
        this.username = Objects.requireNonNull(username, "username").trim();
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash").trim();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
