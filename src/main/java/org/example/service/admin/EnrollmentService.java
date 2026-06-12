package org.example.service.admin;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.entity.EnrollmentDetail;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentDetail> getAllRegistrations();

    Page<EnrollmentDetail> getAllRegistrations(PageRequest request);

    List<EnrollmentDetail> getWaitingRegistrations();

    Page<EnrollmentDetail> getWaitingRegistrations(PageRequest request);

    EnrollmentDetail approveEnrollment(int enrollmentId);

    EnrollmentDetail cancelEnrollment(int enrollmentId);
}
