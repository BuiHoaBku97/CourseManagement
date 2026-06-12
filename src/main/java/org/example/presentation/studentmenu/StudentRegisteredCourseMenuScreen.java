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
        showPagedRegistrations(
                "KHOA HOC DA DANG KY - " + currentStudent.getName(),
                request -> studentPortalService.getRegisteredCourses(currentStudent.getId(), request));
        return ScreenResult.STUDENT_MENU;
    }

    private void showPagedRegistrations(
            String title,
            java.util.function.Function<PageRequest, Page<org.example.entity.EnrollmentDetail>> pageLoader) {
        showPagedTable(title, StudentRegistrationTableRows.HEADERS, "Chua co du lieu", pageLoader, StudentRegistrationTableRows::toRow);
    }

    private StudentAccount requireCurrentStudent() {
        return studentSessionContext.getCurrentStudent()
                .orElseThrow(() -> new IllegalStateException("Khong co thong tin hoc vien dang nhap."));
    }
}
