package org.example.service.student.impl;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.dao.IStudentDao;
import org.example.entity.Student;
import org.example.utils.InputValidator;
import org.example.utils.PasswordHasher;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JdbcStudentService implements org.example.service.student.StudentService {
    private final IStudentDao studentDao;

    public JdbcStudentService(IStudentDao studentDao) {
        this.studentDao = Objects.requireNonNull(studentDao, "studentDao");
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.findAll();
    }

    @Override
    public Page<Student> getStudents(PageRequest request) {
        return studentDao.findAll(request);
    }

    @Override
    public Optional<Student> findStudentById(int id) {
        return studentDao.findById(id);
    }

    @Override
    public Student addStudent(
            String name,
            LocalDate dob,
            boolean sex,
            String email,
            String phone,
            String rawPassword) {
        validateStudent(name, dob, email, rawPassword);
        return studentDao.insert(
                new Student(0, name.trim(), dob, sex, email.trim(), normalizePhone(phone), PasswordHasher.hash(rawPassword), LocalDate.now()));
    }

    @Override
    public Student updateStudentName(int id, String name) {
        validateText(name, "ho ten");
        ensureStudentExists(id);
        if (!studentDao.updateName(id, name.trim())) {
            throw new IllegalStateException("Khong the cap nhat ho ten hoc vien.");
        }
        return requireStudent(id);
    }

    @Override
    public Student updateStudentDob(int id, LocalDate dob) {
        if (dob == null) {
            throw new IllegalArgumentException("Ngay sinh khong duoc de trong.");
        }
        ensureStudentExists(id);
        if (!studentDao.updateDob(id, dob)) {
            throw new IllegalStateException("Khong the cap nhat ngay sinh.");
        }
        return requireStudent(id);
    }

    @Override
    public Student updateStudentSex(int id, boolean sex) {
        ensureStudentExists(id);
        if (!studentDao.updateSex(id, sex)) {
            throw new IllegalStateException("Khong the cap nhat gioi tinh.");
        }
        return requireStudent(id);
    }

    @Override
    public Student updateStudentEmail(int id, String email) {
        validateEmail(email);
        ensureStudentExists(id);
        if (!studentDao.updateEmail(id, email.trim())) {
            throw new IllegalStateException("Khong the cap nhat email.");
        }
        return requireStudent(id);
    }

    @Override
    public Student updateStudentPhone(int id, String phone) {
        ensureStudentExists(id);
        if (!studentDao.updatePhone(id, normalizePhone(phone))) {
            throw new IllegalStateException("Khong the cap nhat so dien thoai.");
        }
        return requireStudent(id);
    }

    @Override
    public Student updateStudentPassword(int id, String rawPassword) {
        validateText(rawPassword, "mat khau");
        ensureStudentExists(id);
        if (!studentDao.updatePassword(id, rawPassword.trim())) {
            throw new IllegalStateException("Khong the cap nhat mat khau.");
        }
        return requireStudent(id);
    }

    @Override
    public boolean deleteStudent(int id) {
        ensureStudentExists(id);
        return studentDao.deleteById(id);
    }

    @Override
    public List<Student> searchStudents(String query) {
        validateText(query, "tu khoa tim kiem");
        return studentDao.search(query.trim());
    }

    @Override
    public Page<Student> searchStudents(String query, PageRequest request) {
        validateText(query, "tu khoa tim kiem");
        return studentDao.search(query.trim(), request);
    }

    @Override
    public List<Student> sortStudentsByName(boolean ascending) {
        return studentDao.sortByName(ascending);
    }

    @Override
    public Page<Student> sortStudentsByName(boolean ascending, PageRequest request) {
        return studentDao.sortByName(ascending, request);
    }

    @Override
    public List<Student> sortStudentsById(boolean ascending) {
        return studentDao.sortById(ascending);
    }

    @Override
    public Page<Student> sortStudentsById(boolean ascending, PageRequest request) {
        return studentDao.sortById(ascending, request);
    }

    private void validateStudent(String name, LocalDate dob, String email, String rawPassword) {
        validateText(name, "ho ten");
        if (dob == null) {
            throw new IllegalArgumentException("Ngay sinh khong duoc de trong.");
        }
        validateEmail(email);
        validateText(rawPassword, "mat khau");
    }

    private void validateText(String value, String fieldName) {
        if (InputValidator.isBlank(value)) {
            throw new IllegalArgumentException("Truong " + fieldName + " khong duoc de trong.");
        }
    }

    private void validateEmail(String email) {
        String normalized = email.trim();
        if (!InputValidator.isValidEmail(normalized)) {
            throw new IllegalArgumentException("Email khong hop le. Dinh dang: chi cho phep chu cai, chu so, '_' , '.' va ket thuc bang @gmail.com.");
        }
    }

    private String normalizePhone(String phone) {
        if (InputValidator.isBlank(phone)) {
            return null;
        }
        String normalized = phone.trim();
        if (!InputValidator.isValidPhone(normalized)) {
            throw new IllegalArgumentException("So dien thoai khong hop le. Co the de trong hoac phai gom 10 chu so va bat dau bang 0.");
        }
        return normalized;
    }

    private void ensureStudentExists(int id) {
        if (studentDao.findById(id).isEmpty()) {
            throw new IllegalStateException("Khong tim thay hoc vien voi id " + id + ".");
        }
    }

    private Student requireStudent(int id) {
        return studentDao.findById(id)
                .orElseThrow(() -> new IllegalStateException("Khong tim thay hoc vien voi id " + id + "."));
    }
}
