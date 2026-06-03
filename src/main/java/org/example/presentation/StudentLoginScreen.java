package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;

public final class StudentLoginScreen extends AbstractMenuScreen {
    public StudentLoginScreen(ConsoleInput input, ConsolePrinter printer) {
        super(input, printer);
    }

    @Override
    public ScreenResult show() {
        renderMenu(
                "DANG NHAP HOC VIEN",
                List.of(
                        "1. Nhap thong tin dang nhap",
                        "0. Quay lai"));
        int choice = input.readChoice("Lua chon: ", 0, 1);
        if (choice == 0) {
            return ScreenResult.STARTUP;
        }

        input.readRequiredLine("Email hoac so dien thoai: ");
        input.readRequiredLine("Mat khau: ");
        printer.printMessage("[todo] xac thuc thanh cong!");
        return ScreenResult.STUDENT_MENU;
    }
}
