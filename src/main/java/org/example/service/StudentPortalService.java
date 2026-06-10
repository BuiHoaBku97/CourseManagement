package org.example.service;

import org.example.entity.Course;
import org.example.entity.EnrollmentDetail;
import org.example.entity.Student;

import java.time.LocalDate;
import java.util.List;

public interface StudentPortalService {
    List<Course> getAvailableCourses();

    List<Course> searchCourses(String query);

    EnrollmentDetail registerCourse(int studentId, int courseId);

    List<EnrollmentDetail> getRegisteredCourses(int studentId);

    List<EnrollmentDetail> sortRegisteredCoursesByCourseName(int studentId, boolean ascending);

    List<EnrollmentDetail> sortRegisteredCoursesByCourseId(int studentId, boolean ascending);

    EnrollmentDetail cancelRegistration(int studentId, int courseId);

    void validateCurrentPassword(int studentId, String currentPassword);

    void validateNewPassword(String newPassword);

    void requestPasswordChangeOtp(int studentId);

    Student updatePassword(
            int studentId,
            String otpCode,
            String currentPassword,
            String newPassword);
}
