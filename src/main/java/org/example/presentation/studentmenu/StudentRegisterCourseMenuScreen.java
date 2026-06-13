package org.example.presentation.studentmenu;

import org.example.entity.EnrollmentDetail;
import org.example.entity.StudentAccount;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.presentation.StudentSessionContext;
import org.example.service.student.StudentPortalService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.Objects;

public final class StudentRegisterCourseMenuScreen extends AbstractMenuScreen {
    private final StudentPortalService studentPortalService;
    private final StudentSessionContext studentSessionContext;

    public StudentRegisterCourseMenuScreen(
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
        Integer courseId = promptPositiveIntOrCancel("Nhap id khoa hoc can dang ky: ");
        if (courseId == null) {
            showMessagePlaceholder("DANG KY KHOA HOC", "Da huy thao tac dang ky khoa hoc.");
            return ScreenResult.STUDENT_MENU;
        }
        try {
            EnrollmentDetail detail = studentPortalService.registerCourse(currentStudent.getId(), courseId);
            showMessagePlaceholder(
                    "DANG KY KHOA HOC",
                    "Da tao dang ky #" + detail.getId() + " voi trang thai " + detail.getStatus().name() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("DANG KY KHOA HOC", exception.getMessage());
        }
        return ScreenResult.STUDENT_MENU;
    }

    private StudentAccount requireCurrentStudent() {
        return studentSessionContext.getCurrentStudent()
                .orElseThrow(() -> new IllegalStateException("Khong co thong tin hoc vien dang nhap."));
    }
}
