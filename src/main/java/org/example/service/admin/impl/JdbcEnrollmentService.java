package org.example.service.admin.impl;

import org.example.dao.IEnrollmentDao;
import org.example.entity.EnrollmentDetail;
import org.example.entity.EnrollmentStatus;

import java.util.List;
import java.util.Objects;

public final class JdbcEnrollmentService implements org.example.service.admin.EnrollmentService {
    private final IEnrollmentDao enrollmentDao;

    public JdbcEnrollmentService(IEnrollmentDao enrollmentDao) {
        this.enrollmentDao = Objects.requireNonNull(enrollmentDao, "enrollmentDao");
    }

    @Override
    public List<EnrollmentDetail> getAllRegistrations() {
        return enrollmentDao.findAllDetails();
    }

    @Override
    public List<EnrollmentDetail> getWaitingRegistrations() {
        return enrollmentDao.findWaitingDetails();
    }

    @Override
    public EnrollmentDetail approveEnrollment(int enrollmentId) {
        EnrollmentDetail detail = requireDetail(enrollmentId);
        if (detail.getStatus() != EnrollmentStatus.WAITING) {
            throw new IllegalStateException("Chi co dang ky WAITING moi co the duyet.");
        }
        if (!enrollmentDao.approve(enrollmentId)) {
            throw new IllegalStateException("Khong the duyet dang ky.");
        }
        return requireDetail(enrollmentId);
    }

    @Override
    public EnrollmentDetail cancelEnrollment(int enrollmentId) {
        EnrollmentDetail detail = requireDetail(enrollmentId);
        if (detail.getStatus() == EnrollmentStatus.CANCEL) {
            throw new IllegalStateException("Dang ky da bi huy.");
        }
        if (!enrollmentDao.cancel(enrollmentId)) {
            throw new IllegalStateException("Khong the huy dang ky.");
        }
        return requireDetail(enrollmentId);
    }

    private EnrollmentDetail requireDetail(int enrollmentId) {
        return enrollmentDao.findAllDetails().stream()
                .filter(detail -> detail.getId() == enrollmentId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Khong tim thay dang ky voi id " + enrollmentId + "."));
    }
}
