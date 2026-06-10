package org.example.dao;

import org.example.entity.StudentAccount;

import java.util.Optional;

public interface IStudentAccountDao {
    Optional<StudentAccount> findByLoginId(String loginId);
}
