package org.example.presentation.studentmenu;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.entity.StudentAccount;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.presentation.StudentSessionContext;
import org.example.service.student.StudentPortalService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;
import java.util.Objects;

public final class StudentCourseMenuScreen extends AbstractMenuScreen {
    private final StudentPortalService studentPortalService;
    private final StudentSessionContext studentSessionContext;

    public StudentCourseMenuScreen(
            ConsoleInput input,
            ConsolePrinter printer,
            StudentPortalService studentPortalService,
            StudentSessionContext studentSessionContext) {
        super(input, printer);
        this.studentPortalService = Objects.requireNonNull(studentPortalService, "studentPortalService");
        this.studentSessionContext = Objects.requireNonNull(studentSessionContext, "studentSessionContext");
    }

    @Override
    public ScreenResult show() {
        StudentAccount currentStudent = requireCurrentStudent();
        while (true) {
            int choice = promptChoice(
                    "XEM DANH SACH KHOA HOC - " + currentStudent.getName(),
                    List.of(
                            "1. Xem danh sach khoa hoc dang co",
                            "2. Tim kiem khoa hoc theo ten",
                            "0. Quay lai menu Hoc vien"),
                    0,
                    2);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.STUDENT_MENU;
                }
                case 1 -> showPagedCourses(
                        "DANH SACH KHOA HOC",
                        studentPortalService::getAvailableCourses,
                        "Chua co khoa hoc nao.");
                case 2 -> handleSearchCourses();
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void handleSearchCourses() {
        try {
            String query = promptRequiredLineOrCancel("Nhap tu khoa tim kiem: ");
            if (query == null) {
                showMessagePlaceholder("TIM KIEM KHOA HOC", "Da huy thao tac tim kiem khoa hoc.");
                return;
            }
            showPagedCourses(
                    "TIM KIEM KHOA HOC",
                    request -> studentPortalService.searchCourses(query, request),
                    "Khong tim thay khoa hoc phu hop.");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("TIM KIEM KHOA HOC", exception.getMessage());
        }
    }

    private void showPagedCourses(
            String title,
            java.util.function.Function<PageRequest, Page<org.example.entity.Course>> pageLoader,
            String emptyMessage) {
        showPagedTable(title, StudentCourseTableRows.HEADERS, emptyMessage, pageLoader, StudentCourseTableRows::toRow);
    }

    private StudentAccount requireCurrentStudent() {
        return studentSessionContext.getCurrentStudent()
                .orElseThrow(() -> new IllegalStateException("Khong co thong tin hoc vien dang nhap."));
    }
}
