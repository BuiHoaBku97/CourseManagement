package org.example.service;

import org.example.entity.StudentAccount;

import java.util.Optional;

public interface AuthenticationService {
    boolean authenticateAdmin(String username, String rawPassword);

    boolean authenticateStudent(String loginId, String rawPassword);

    default Optional<StudentAccount> authenticateStudentAccount(String loginId, String rawPassword) {
        return Optional.empty();
    }
}
