package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;

public final class AdminLoginScreen extends AbstractMenuScreen {
    public AdminLoginScreen(ConsoleInput input, ConsolePrinter printer) {
        super(input, printer);
    }

    @Override
    public ScreenResult show() {
        renderMenu(
                "DANG NHAP ADMIN",
                List.of(
                        "1. Nhap thong tin dang nhap",
                        "0. Quay lai"));
        int choice = input.readChoice("Lua chon: ", 0, 1);
        if (choice == 0) {
            return ScreenResult.STARTUP;
        }

        input.readRequiredLine("Ten dang nhap: ");
        input.readRequiredLine("Mat khau: ");
        printer.printMessage("[todo] xac thuc thanh cong!");
        return ScreenResult.ADMIN_MENU;
    }
}
