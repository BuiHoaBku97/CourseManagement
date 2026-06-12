package org.example.presentation.adminmenu;

import org.example.entity.EnrollmentDetail;

import java.util.List;

final class EnrollmentTableRows {
    static final List<String> HEADERS =
            List.of("ID", "Course ID", "Ten khoa hoc", "Student ID", "Hoc vien", "Trang thai", "Ngay dang ky");

    private EnrollmentTableRows() {
    }

    static List<String> toRow(EnrollmentDetail detail) {
        return List.of(
                String.valueOf(detail.getId()),
                String.valueOf(detail.getCourseId()),
                detail.getCourseName(),
                String.valueOf(detail.getStudentId()),
                detail.getStudentName(),
                detail.getStatus().name(),
                detail.getRegisteredAt().toString());
    }
}
