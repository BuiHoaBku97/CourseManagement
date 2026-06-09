package org.example.presentation;

import org.example.entity.StudentAccount;

import java.util.Objects;
import java.util.Optional;

public final class StudentSessionContext {
    private StudentAccount currentStudent;

    public void setCurrentStudent(StudentAccount studentAccount) {
        this.currentStudent = Objects.requireNonNull(studentAccount, "studentAccount");
    }

    public Optional<StudentAccount> getCurrentStudent() {
        return Optional.ofNullable(currentStudent);
    }

    public void clear() {
        currentStudent = null;
    }
}
