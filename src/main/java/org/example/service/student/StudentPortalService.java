package org.example.service.student;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.entity.Course;
import org.example.entity.EnrollmentDetail;
import org.example.entity.Student;

import java.util.List;

public interface StudentPortalService {
    List<Course> getAvailableCourses();

    Page<Course> getAvailableCourses(PageRequest request);

    List<Course> searchCourses(String query);

    Page<Course> searchCourses(String query, PageRequest request);

    EnrollmentDetail registerCourse(int studentId, int courseId);

    List<EnrollmentDetail> getRegisteredCourses(int studentId);

    Page<EnrollmentDetail> getRegisteredCourses(int studentId, PageRequest request);

    List<EnrollmentDetail> sortRegisteredCoursesByCourseName(int studentId, boolean ascending);

    Page<EnrollmentDetail> sortRegisteredCoursesByCourseName(int studentId, boolean ascending, PageRequest request);

    List<EnrollmentDetail> sortRegisteredCoursesByCourseId(int studentId, boolean ascending);

    Page<EnrollmentDetail> sortRegisteredCoursesByCourseId(int studentId, boolean ascending, PageRequest request);

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
