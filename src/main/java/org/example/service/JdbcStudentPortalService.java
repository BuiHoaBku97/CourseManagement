package org.example.service;

import org.example.dao.CourseDao;
import org.example.dao.EnrollmentDao;
import org.example.dao.StudentDao;
import org.example.entity.Course;
import org.example.entity.EnrollmentDetail;
import org.example.entity.EnrollmentStatus;
import org.example.entity.Student;
import org.example.utils.InputValidator;
import org.example.utils.PasswordHasher;

import java.util.List;
import java.util.Objects;

public final class JdbcStudentPortalService implements StudentPortalService {
    private final CourseDao courseDao;
    private final EnrollmentDao enrollmentDao;
    private final StudentDao studentDao;

    public JdbcStudentPortalService(CourseDao courseDao, EnrollmentDao enrollmentDao, StudentDao studentDao) {
        this.courseDao = Objects.requireNonNull(courseDao, "courseDao");
        this.enrollmentDao = Objects.requireNonNull(enrollmentDao, "enrollmentDao");
        this.studentDao = Objects.requireNonNull(studentDao, "studentDao");
    }

    @Override
    public List<Course> getAvailableCourses() {
        return courseDao.findAll();
    }

    @Override
    public List<Course> searchCourses(String query) {
        if (InputValidator.isBlank(query)) {
            throw new IllegalArgumentException("Tu khoa tim kiem khong duoc de trong.");
        }
        return courseDao.searchByName(query);
    }

    @Override
    public EnrollmentDetail registerCourse(int studentId, int courseId) {
        ensureStudentExists(studentId);
        ensureCourseExists(courseId);
        var existingEnrollment = enrollmentDao.findDetailByStudentAndCourse(studentId, courseId);
        if (existingEnrollment.isPresent()) {
            EnrollmentDetail existing = existingEnrollment.get();
            if (existing.getStatus() != EnrollmentStatus.CANCEL) {
                throw new IllegalStateException("Ban da co dang ky cho khoa hoc nay.");
            }
            if (!enrollmentDao.updateStatusByStudentAndCourse(studentId, courseId, EnrollmentStatus.WAITING)) {
                throw new IllegalStateException("Khong the khoi phuc dang ky.");
            }
            return enrollmentDao.findDetailByStudentAndCourse(studentId, courseId)
                    .orElseThrow(() -> new IllegalStateException("Khong the xac minh dang ky sau khi cap nhat."));
        }
        return enrollmentDao.insertWaiting(studentId, courseId);
    }

    @Override
    public List<EnrollmentDetail> getRegisteredCourses(int studentId) {
        ensureStudentExists(studentId);
        return enrollmentDao.findByStudentId(studentId);
    }

    @Override
    public List<EnrollmentDetail> sortRegisteredCoursesByCourseName(int studentId, boolean ascending) {
        ensureStudentExists(studentId);
        return enrollmentDao.findByStudentIdSortedByCourseName(studentId, ascending);
    }

    @Override
    public List<EnrollmentDetail> sortRegisteredCoursesByCourseId(int studentId, boolean ascending) {
        ensureStudentExists(studentId);
        return enrollmentDao.findByStudentIdSortedByCourseId(studentId, ascending);
    }

    @Override
    public EnrollmentDetail cancelRegistration(int studentId, int courseId) {
        ensureStudentExists(studentId);
        ensureCourseExists(courseId);
        EnrollmentDetail detail = enrollmentDao.findDetailByStudentAndCourse(studentId, courseId)
                .orElseThrow(() -> new IllegalStateException("Khong tim thay dang ky cho khoa hoc nay."));
        if (detail.getStatus() == EnrollmentStatus.CONFIRM) {
            throw new IllegalStateException("Dang ky da duoc xac nhan nen khong the huy.");
        }
        if (detail.getStatus() == EnrollmentStatus.CANCEL) {
            throw new IllegalStateException("Dang ky da o trang thai huy.");
        }
        enrollmentDao.updateStatusByStudentAndCourse(studentId, courseId, EnrollmentStatus.CANCEL);
        return enrollmentDao.findDetailByStudentAndCourse(studentId, courseId).orElse(detail);
    }

    @Override
    public Student updatePassword(int studentId, String currentPassword, String newPassword) {
        if (InputValidator.isBlank(newPassword)) {
            throw new IllegalArgumentException("Mat khau moi khong duoc de trong.");
        }
        Student currentStudent = ensureStudentExists(studentId);
        if (!PasswordHasher.matches(currentPassword, currentStudent.getPasswordHash())) {
            throw new IllegalStateException("Mat khau hien tai khong chinh xac.");
        }
        if (!studentDao.updatePassword(studentId, newPassword)) {
            throw new IllegalStateException("Khong the cap nhat mat khau.");
        }
        return ensureStudentExists(studentId);
    }

    private Student ensureStudentExists(int studentId) {
        return studentDao.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Khong tim thay hoc vien voi id " + studentId + "."));
    }

    private Course ensureCourseExists(int courseId) {
        return courseDao.findById(courseId)
                .orElseThrow(() -> new IllegalStateException("Khong tim thay khoa hoc voi id " + courseId + "."));
    }
}
