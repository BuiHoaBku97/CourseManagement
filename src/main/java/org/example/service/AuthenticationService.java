package org.example.service;

public interface AuthenticationService {
    boolean authenticateAdmin(String username, String rawPassword);

    boolean authenticateStudent(String loginId, String rawPassword);
}
