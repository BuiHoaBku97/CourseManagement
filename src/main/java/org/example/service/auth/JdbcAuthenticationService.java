package org.example.service.auth;

import org.example.dao.AdminAccountDao;
import org.example.dao.StudentAccountDao;
import org.example.entity.AdminAccount;
import org.example.entity.StudentAccount;
import org.example.utils.PasswordHasher;

import java.util.Optional;
import java.util.Objects;

public final class JdbcAuthenticationService implements AuthenticationService {
    private final AdminAccountDao adminAccountDao;
    private final StudentAccountDao studentAccountDao;

    private static AdminAccount defaultAdmin ;

    public JdbcAuthenticationService(
            AdminAccountDao adminAccountDao, StudentAccountDao studentAccountDao) {
        this.adminAccountDao = Objects.requireNonNull(adminAccountDao, "adminAccountDao");
        this.studentAccountDao = Objects.requireNonNull(studentAccountDao, "studentAccountDao");
        defaultAdmin = adminAccountDao.getDefaultAdmin();
    }

    @Override
    public boolean authenticateAdmin(String username, String rawPassword) {
        if ( username.equals(defaultAdmin.getUsername()) ){
            return PasswordHasher.matches(rawPassword, defaultAdmin.getPasswordHash());
        }

        AdminAccount adminAccount = adminAccountDao.findByUsername(username).orElse(null);
        return adminAccount != null
                && PasswordHasher.matches(rawPassword, adminAccount.getPasswordHash());
    }

    @Override
    public boolean authenticateStudent(String loginId, String rawPassword) {
        return authenticateStudentAccount(loginId, rawPassword).isPresent();
    }

    @Override
    public Optional<StudentAccount> authenticateStudentAccount(String loginId, String rawPassword) {
        StudentAccount studentAccount = studentAccountDao.findByLoginId(loginId).orElse(null);
        if (studentAccount == null) {
            return Optional.empty();
        }
        if (!PasswordHasher.matches(rawPassword, studentAccount.getPasswordHash())) {
            return Optional.empty();
        }
        return Optional.of(studentAccount);
    }
}
