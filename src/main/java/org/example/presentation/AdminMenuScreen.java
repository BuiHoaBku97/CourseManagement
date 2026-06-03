package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;

public final class AdminMenuScreen extends AbstractMenuScreen {
    public AdminMenuScreen(ConsoleInput input, ConsolePrinter printer) {
        super(input, printer);
    }

    @Override
    public ScreenResult show() {
        renderMenu(
                "MENU QUAN TRI VIEN",
                List.of(
                        "1. Quan ly khoa hoc",
                        "2. Quan ly hoc vien",
                        "3. Quan ly dang ky khoa hoc",
                        "4. Thong ke hoc vien theo khoa hoc",
                        "0. Dang xuat"));
        int choice = input.readChoice("Lua chon: ", 0, 4);
        return switch (choice) {
            case 1 -> ScreenResult.COURSE_MENU;
            case 2 -> ScreenResult.STUDENT_MANAGEMENT_MENU;
            case 3 -> ScreenResult.ENROLLMENT_MENU;
            case 4 -> ScreenResult.STATISTICS_MENU;
            default -> ScreenResult.STARTUP;
        };
    }
}
