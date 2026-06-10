package org.example.presentation.studentmenu;

import org.example.entity.EnrollmentDetail;
import org.example.entity.StudentAccount;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.presentation.StudentSessionContext;
import org.example.service.student.StudentPortalService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StudentRegisteredCourseMenuScreen extends AbstractMenuScreen {
    private final StudentPortalService studentPortalService;
    private final StudentSessionContext studentSessionContext;

    public StudentRegisteredCourseMenuScreen(
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
        showRegistrations(
                "KHOA HOC DA DANG KY - " + currentStudent.getName(),
                studentPortalService.getRegisteredCourses(currentStudent.getId()));
        return ScreenResult.STUDENT_MENU;
    }

    private void showRegistrations(String title, List<EnrollmentDetail> details) {
        List<List<String>> rows = new ArrayList<>();
        for (EnrollmentDetail detail : details) {
            rows.add(
                    List.of(
                            String.valueOf(detail.getId()),
                            String.valueOf(detail.getCourseId()),
                            detail.getCourseName(),
                            detail.getStatus().name(),
                            detail.getRegisteredAt().toString()));
        }
        if (rows.isEmpty()) {
            rows.add(List.of("Chua co du lieu", "", "", "", ""));
        }
        showTable(title, List.of("ID", "Course ID", "Ten khoa hoc", "Trang thai", "Ngay dang ky"), rows);
    }

    private StudentAccount requireCurrentStudent() {
        return studentSessionContext.getCurrentStudent()
                .orElseThrow(() -> new IllegalStateException("Khong co thong tin hoc vien dang nhap."));
    }
}
