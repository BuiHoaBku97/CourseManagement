package org.example.dao;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.entity.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IStudentDao {
    List<Student> findAll();

    Page<Student> findAll(PageRequest request);

    Optional<Student> findById(int id);

    Student insert(Student student);

    boolean updateName(int id, String name);

    boolean updateDob(int id, LocalDate dob);

    boolean updateSex(int id, boolean sex);

    boolean updateEmail(int id, String email);

    boolean updatePhone(int id, String phone);

    boolean updatePassword(int id, String rawPassword);

    boolean deleteById(int id);

    List<Student> search(String query);

    Page<Student> search(String query, PageRequest request);

    List<Student> sortByName(boolean ascending);

    Page<Student> sortByName(boolean ascending, PageRequest request);

    List<Student> sortById(boolean ascending);

    Page<Student> sortById(boolean ascending, PageRequest request);

    long countAll();
}
