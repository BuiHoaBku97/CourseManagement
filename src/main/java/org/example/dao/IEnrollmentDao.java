package org.example.dao;

import org.example.entity.Enrollment;
import org.example.entity.EnrollmentDetail;
import org.example.entity.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

public interface IEnrollmentDao {
    List<EnrollmentDetail> findAllDetails();

    List<EnrollmentDetail> findWaitingDetails();

    List<EnrollmentDetail> findByStudentId(int studentId);

    List<EnrollmentDetail> findByStudentIdSortedByCourseName(int studentId, boolean ascending);

    List<EnrollmentDetail> findByStudentIdSortedByCourseId(int studentId, boolean ascending);

    Optional<EnrollmentDetail> findDetailByStudentAndCourse(int studentId, int courseId);

    EnrollmentDetail insertWaiting(int studentId, int courseId);

    boolean updateStatusByStudentAndCourse(int studentId, int courseId, EnrollmentStatus status);

    Optional<Enrollment> findById(int id);

    boolean approve(int id);

    boolean cancel(int id);
}
