package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;
import org.example.service.AuthenticationService;

import java.util.List;
import java.util.Objects;

public final class StudentLoginScreen extends AbstractMenuScreen {
    private final AuthenticationService authenticationService;

    public StudentLoginScreen( ConsoleInput input, ConsolePrinter printer, AuthenticationService authenticationService) {
        super(input, printer);
        this.authenticationService = Objects.requireNonNull(authenticationService, "authenticationService");
    }

    @Override
    public ScreenResult show() {
        int choice = promptChoice("DANG NHAP HOC VIEN", List.of("1. Dang nhap", "0. Quay lai"), 0, 1);
        if (choice == 0) {
            return ScreenResult.STARTUP;
        }

        while (true) {
            String loginId = input.readRequiredLine("Email hoac so dien thoai: ");
            String password = input.readRequiredLine("Mat khau: ");

            try {
                if (authenticationService.authenticateStudent(loginId, password)) {
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
