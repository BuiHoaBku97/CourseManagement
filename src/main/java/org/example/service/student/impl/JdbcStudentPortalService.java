package org.example.service.student.impl;

import org.example.dao.ICourseDao;
import org.example.dao.IEnrollmentDao;
import org.example.dao.IStudentDao;
import org.example.entity.Course;
import org.example.entity.EnrollmentDetail;
import org.example.entity.EnrollmentStatus;
import org.example.entity.Student;
import org.example.service.student.OtpSender;
import org.example.utils.InputValidator;
import org.example.utils.PasswordHasher;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class JdbcStudentPortalService implements org.example.service.student.StudentPortalService {
    private static final Duration OTP_VALIDITY = Duration.ofMinutes(5);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ICourseDao courseDao;
    private final IEnrollmentDao enrollmentDao;
    private final IStudentDao studentDao;
    private final OtpSender otpSender;
    private final Map<Integer, PasswordOtpChallenge> passwordOtpChallenges = new HashMap<>();

    public JdbcStudentPortalService(
            ICourseDao courseDao,
            IEnrollmentDao enrollmentDao,
            IStudentDao studentDao,
            OtpSender otpSender) {
        this.courseDao = Objects.requireNonNull(courseDao, "courseDao");
        this.enrollmentDao = Objects.requireNonNull(enrollmentDao, "enrollmentDao");
        this.studentDao = Objects.requireNonNull(studentDao, "studentDao");
        this.otpSender = Objects.requireNonNull(otpSender, "otpSender");
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
    public void validateCurrentPassword(int studentId, String currentPassword) {
        Student currentStudent = ensureStudentExists(studentId);
        if (!PasswordHasher.matches(currentPassword, currentStudent.getPasswordHash())) {
            throw new IllegalStateException("Mat khau hien tai khong chinh xac.");
        }
    }

    @Override
    public void validateNewPassword(String newPassword) {
        if (InputValidator.isBlank(newPassword)) {
            throw new IllegalArgumentException("Mat khau moi khong duoc de trong.");
        }
    }

    @Override
    public void requestPasswordChangeOtp(int studentId) {
        Student currentStudent = ensureStudentExists(studentId);
        String otpCode = generateOtpCode();
        otpSender.sendPasswordChangeOtp(currentStudent.getEmail(), otpCode);
        passwordOtpChallenges.put(studentId, new PasswordOtpChallenge(otpCode, Instant.now()));
    }

    @Override
    public Student updatePassword(
            int studentId,
            String otpCode,
            String currentPassword,
            String newPassword) {
        Student currentStudent = ensureStudentExists(studentId);
        PasswordOtpChallenge challenge =
                passwordOtpChallenges.get(studentId);
        if (challenge == null) {
            throw new IllegalStateException("Vui long yeu cau OTP truoc khi doi mat khau.");
        }
        if (challenge.isExpired()) {
            passwordOtpChallenges.remove(studentId);
            throw new IllegalStateException("OTP da het han. Vui long yeu cau ma moi.");
        }
        if (!challenge.otpCode.equals(otpCode == null ? null : otpCode.trim())) {
            throw new IllegalStateException("Ma OTP khong chinh xac.");
        }
        validateCurrentPassword(studentId, currentPassword);
        validateNewPassword(newPassword);
        if (!studentDao.updatePassword(studentId, newPassword)) {
            throw new IllegalStateException("Khong the cap nhat mat khau.");
        }
        passwordOtpChallenges.remove(studentId);
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

    private String generateOtpCode() {
        int value = RANDOM.nextInt(900000) + 100000;
        return String.valueOf(value);
    }

    private record PasswordOtpChallenge(String otpCode, Instant issuedAt) {
        private boolean isExpired() {
            return issuedAt.plus(OTP_VALIDITY).isBefore(Instant.now());
        }
    }
}
