package org.example.service.admin;

import org.example.entity.EnrollmentDetail;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentDetail> getAllRegistrations();

    List<EnrollmentDetail> getWaitingRegistrations();

    EnrollmentDetail approveEnrollment(int enrollmentId);

    EnrollmentDetail cancelEnrollment(int enrollmentId);
}
