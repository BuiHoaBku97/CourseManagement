package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;

public final class StartupScreen extends AbstractMenuScreen {
    public StartupScreen(ConsoleInput input, ConsolePrinter printer) {
        super(input, printer);
    }

    @Override
    public ScreenResult show() {
        renderMenu(
                "HE THONG QUAN LY KHOA HOC",
                List.of(
                        "1. Dang nhap Admin",
                        "2. Dang nhap Hoc vien",
                        "0. Thoat chuong trinh"));
        int choice = input.readChoice("Lua chon: ", 0, 2);
        return switch (choice) {
            case 1 -> ScreenResult.ADMIN_LOGIN;
            case 2 -> ScreenResult.STUDENT_LOGIN;
            default -> ScreenResult.EXIT;
        };
    }
}
