package org.example.dao;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.entity.Enrollment;
import org.example.entity.EnrollmentDetail;
import org.example.entity.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

public interface IEnrollmentDao {
    List<EnrollmentDetail> findAllDetails();

    Page<EnrollmentDetail> findAllDetails(PageRequest request);

    List<EnrollmentDetail> findWaitingDetails();

    Page<EnrollmentDetail> findWaitingDetails(PageRequest request);

    List<EnrollmentDetail> findByStudentId(int studentId);

    Page<EnrollmentDetail> findByStudentId(int studentId, PageRequest request);

    List<EnrollmentDetail> findByStudentIdSortedByCourseName(int studentId, boolean ascending);

    Page<EnrollmentDetail> findByStudentIdSortedByCourseName(int studentId, boolean ascending, PageRequest request);

    List<EnrollmentDetail> findByStudentIdSortedByCourseId(int studentId, boolean ascending);

    Page<EnrollmentDetail> findByStudentIdSortedByCourseId(int studentId, boolean ascending, PageRequest request);

    Optional<EnrollmentDetail> findDetailByStudentAndCourse(int studentId, int courseId);

    EnrollmentDetail insertWaiting(int studentId, int courseId);

    boolean updateStatusByStudentAndCourse(int studentId, int courseId, EnrollmentStatus status);

    Optional<Enrollment> findById(int id);

    boolean approve(int id);

    boolean cancel(int id);
}
