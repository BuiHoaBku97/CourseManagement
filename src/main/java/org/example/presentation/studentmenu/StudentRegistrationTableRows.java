package org.example.presentation.studentmenu;

import org.example.entity.EnrollmentDetail;

import java.util.List;

final class StudentRegistrationTableRows {
    static final List<String> HEADERS =
            List.of("ID", "Course ID", "Ten khoa hoc", "Trang thai", "Ngay dang ky");

    private StudentRegistrationTableRows() {
    }

    static List<String> toRow(EnrollmentDetail detail) {
        return List.of(
                String.valueOf(detail.getId()),
                String.valueOf(detail.getCourseId()),
                detail.getCourseName(),
                detail.getStatus().name(),
                detail.getRegisteredAt().toString());
    }
}
