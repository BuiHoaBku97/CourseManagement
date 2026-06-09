package org.example.service;

import org.example.entity.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getAllStudents();

    Optional<Student> findStudentById(int id);

    Student addStudent(
            String name,
            LocalDate dob,
            boolean sex,
            String email,
            String phone,
            String rawPassword);

    Student updateStudentName(int id, String name);

    Student updateStudentDob(int id, LocalDate dob);

    Student updateStudentSex(int id, boolean sex);

    Student updateStudentEmail(int id, String email);

    Student updateStudentPhone(int id, String phone);

    Student updateStudentPassword(int id, String rawPassword);

    boolean deleteStudent(int id);

    List<Student> searchStudents(String query);

    List<Student> sortStudentsByName(boolean ascending);

    List<Student> sortStudentsById(boolean ascending);
}
