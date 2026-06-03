package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;
import org.example.service.AuthenticationService;

import java.util.List;
import java.util.Objects;

public final class AdminLoginScreen extends AbstractMenuScreen {
    private final AuthenticationService authenticationService;

    public AdminLoginScreen(
            ConsoleInput input, ConsolePrinter printer, AuthenticationService authenticationService) {
        super(input, printer);
        this.authenticationService =
                Objects.requireNonNull(authenticationService, "authenticationService");
    }

    @Override
    public ScreenResult show() {
        renderMenu("DANG NHAP ADMIN", List.of("1. Dang nhap", "0. Quay lai"));
        int choice = input.readChoice("Lua chon: ", 0, 1);
        if (choice == 0) {
            return ScreenResult.STARTUP;
        }

        while (true) {
            String username = input.readRequiredLine("Ten dang nhap: ");
            String password = input.readRequiredLine("Mat khau: ");

            try {
                if (authenticationService.authenticateAdmin(username, password)) {
                    printer.printMessage("Dang nhap admin thanh cong.");
                    return ScreenResult.ADMIN_MENU;
                }
                printer.printMessage("Thong tin dang nhap khong chinh xac. Vui long nhap lai.");
            } catch (IllegalStateException exception) {
                printer.printMessage("Khong the xac thuc admin: " + exception.getMessage());
                return ScreenResult.STARTUP;
            }
        }
    }
}
