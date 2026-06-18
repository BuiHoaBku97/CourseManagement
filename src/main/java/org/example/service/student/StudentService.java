package org.example.service.student;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.entity.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getAllStudents();

    Page<Student> getStudents(PageRequest request);

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

    boolean hasActiveEnrollments(int studentId);

    boolean deleteStudent(int id);

    List<Student> searchStudents(String query);

    Page<Student> searchStudents(String query, PageRequest request);

    List<Student> sortStudentsByName(boolean ascending);

    Page<Student> sortStudentsByName(boolean ascending, PageRequest request);

    List<Student> sortStudentsById(boolean ascending);

    Page<Student> sortStudentsById(boolean ascending, PageRequest request);
}
