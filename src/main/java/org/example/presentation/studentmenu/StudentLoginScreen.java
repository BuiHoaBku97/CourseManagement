package org.example.presentation.studentmenu;

import org.example.entity.StudentAccount;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.presentation.StudentSessionContext;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;
import org.example.service.auth.AuthenticationService;

import java.util.List;
import java.util.Objects;

public final class StudentLoginScreen extends AbstractMenuScreen {
    private final AuthenticationService authenticationService;
    private final StudentSessionContext studentSessionContext;

    public StudentLoginScreen(
            ConsoleInput input,
            ConsolePrinter printer,
            AuthenticationService authenticationService,
            StudentSessionContext studentSessionContext) {
        super(input, printer);
        this.authenticationService = Objects.requireNonNull(authenticationService, "authenticationService");
        this.studentSessionContext = Objects.requireNonNull(studentSessionContext, "studentSessionContext");
    }

    @Override
    public ScreenResult show() {
        int choice = promptChoice("DANG NHAP HOC VIEN", List.of("1. Dang nhap", "0. Quay lai"), 0, 1);
        if (choice == 0) {
            return ScreenResult.STARTUP;
        }

        while (true) {
            String loginId = promptRequiredLineOrCancel("Email hoc vien: ");
            if (loginId == null) {
                return ScreenResult.STARTUP;
            }
            String password = promptRequiredLineOrCancel("Mat khau: ");
            if (password == null) {
                return ScreenResult.STARTUP;
            }

            try {
                StudentAccount studentAccount =
                        authenticationService.authenticateStudentAccount(loginId, password).orElse(null);
                if (studentAccount != null) {
                    studentSessionContext.setCurrentStudent(studentAccount);
                    printer.printMessage("Dang nhap hoc vien thanh cong.");
                    return ScreenResult.STUDENT_MENU;
                }
                printer.printMessage("Thong tin dang nhap khong chinh xac. Vui long nhap lai.");
            } catch (IllegalStateException exception) {
                printer.printMessage("Khong the xac thuc hoc vien: " + exception.getMessage());
                return ScreenResult.STARTUP;
            }
        }
    }
}
