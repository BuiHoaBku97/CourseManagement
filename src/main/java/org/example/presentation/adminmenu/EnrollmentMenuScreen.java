package org.example.presentation.adminmenu;

import org.example.entity.EnrollmentDetail;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.EnrollmentService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class EnrollmentMenuScreen extends AbstractMenuScreen {
    private final EnrollmentService enrollmentService;

    public EnrollmentMenuScreen(ConsoleInput input, ConsolePrinter printer, EnrollmentService enrollmentService) {
        super(input, printer);
        this.enrollmentService = Objects.requireNonNull(enrollmentService, "enrollmentService");
    }

    @Override
    public ScreenResult show() {
        while (true) {
            int choice = promptChoice(
                    "QUAN LY DANG KY KHOA HOC",
                    List.of(
                            "1. Hien thi danh sach sinh vien dang ky theo tung khoa hoc",
                            "2. Duyet sinh vien dang ky khoa hoc",
                            "3. Xoa sinh vien khoi khoa hoc",
                            "0. Quay lai menu Admin"),
                    0,
                    3);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.ADMIN_MENU;
                }
                case 1 -> showEnrollments("DANH SACH DANG KY THEO KHOA HOC", enrollmentService.getAllRegistrations());
                case 2 -> handleApprove();
                case 3 -> handleCancel();
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void handleApprove() {
        showEnrollments("DANH SACH DANG KY CHO DUYET", enrollmentService.getWaitingRegistrations());
        int id = promptPositiveInt("Nhap id dang ky can duyet: ");
        try {
            EnrollmentDetail detail = enrollmentService.approveEnrollment(id);
            showMessagePlaceholder(
                    "DUYET SINH VIEN DANG KY KHOA HOC",
                    "Da duyet dang ky #" + detail.getId() + " cho hoc vien " + detail.getStudentName() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("DUYET SINH VIEN DANG KY KHOA HOC", exception.getMessage());
        }
    }

    private void handleCancel() {
        showEnrollments("DANH SACH DANG KY", enrollmentService.getAllRegistrations());
        int id = promptPositiveInt("Nhap id dang ky can xoa: ");
        try {
            EnrollmentDetail detail = enrollmentService.cancelEnrollment(id);
            showMessagePlaceholder(
                    "XOA SINH VIEN KHOI KHOA HOC",
                    "Da huy dang ky #" + detail.getId() + " cua hoc vien " + detail.getStudentName() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("XOA SINH VIEN KHOI KHOA HOC", exception.getMessage());
        }
    }

    private void showEnrollments(String title, List<EnrollmentDetail> details) {
        List<List<String>> rows = new ArrayList<>();
        for (EnrollmentDetail detail : details) {
            rows.add(
                    List.of(
                            String.valueOf(detail.getId()),
                            String.valueOf(detail.getCourseId()),
                            detail.getCourseName(),
                            String.valueOf(detail.getStudentId()),
                            detail.getStudentName(),
                            detail.getStatus().name(),
                            detail.getRegisteredAt().toString()));
        }
        if (rows.isEmpty()) {
            rows.add(List.of("Chua co du lieu", "", "", "", "", "", ""));
        }
        showTable(
                title,
                List.of("ID", "Course ID", "Ten khoa hoc", "Student ID", "Hoc vien", "Trang thai", "Ngay dang ky"),
                rows);
    }
}
