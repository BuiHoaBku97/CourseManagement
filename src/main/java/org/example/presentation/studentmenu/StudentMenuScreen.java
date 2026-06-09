package org.example.presentation.studentmenu;

import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.presentation.StudentSessionContext;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;
import java.util.Objects;

public final class StudentMenuScreen extends AbstractMenuScreen {
    private final StudentSessionContext studentSessionContext;

    public StudentMenuScreen(
            ConsoleInput input,
            ConsolePrinter printer,
            StudentSessionContext studentSessionContext) {
        super(input, printer);
        this.studentSessionContext = Objects.requireNonNull(studentSessionContext, "studentSessionContext");
    }

    @Override
    public ScreenResult show() {
        String studentName =
                studentSessionContext.getCurrentStudent()
                        .orElseThrow(() -> new IllegalStateException("Khong co thong tin hoc vien dang nhap."))
                        .getName();
        int choice = promptChoice(
                "MENU HOC VIEN - " + studentName,
                List.of(
                        "1. Xem danh sach khoa hoc",
                        "2. Dang ky khoa hoc",
                        "3. Xem khoa hoc da dang ky",
                        "4. Huy dang ky khoa hoc",
                        "5. Cap nhat mat khau tai khoan",
                        "0. Dang xuat"),
                0,
                5);
        return switch (choice) {
            case 1 -> ScreenResult.STUDENT_COURSE_MENU;
            case 2 -> ScreenResult.STUDENT_REGISTER_COURSE_MENU;
            case 3 -> ScreenResult.STUDENT_REGISTERED_COURSE_MENU;
            case 4 -> ScreenResult.STUDENT_CANCEL_REGISTRATION_MENU;
            case 5 -> ScreenResult.STUDENT_PASSWORD_MENU;
            default -> {
                studentSessionContext.clear();
                yield ScreenResult.STARTUP;
            }
        };
    }
}
