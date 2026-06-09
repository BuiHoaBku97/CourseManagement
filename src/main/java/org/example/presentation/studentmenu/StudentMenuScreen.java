package org.example.presentation.studentmenu;

import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;

public final class StudentMenuScreen extends AbstractMenuScreen {
    public StudentMenuScreen(ConsoleInput input, ConsolePrinter printer) {
        super(input, printer);
    }

    @Override
    public ScreenResult show() {
        while (true) {
            int choice = promptChoice(
                    "MENU HOC VIEN",
                    List.of(
                            "1. Xem danh sach khoa hoc",
                            "2. Dang ky khoa hoc",
                            "3. Xem khoa hoc da dang ky",
                            "4. Huy dang ky khoa hoc",
                            "5. Cap nhat mat khau",
                            "0. Dang xuat"),
                    0,
                    5);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.STARTUP;
                }
                case 1 -> showListPlaceholder(
                        "DANH SACH KHOA HOC",
                        List.of("ID", "Ten khoa hoc", "Thoi luong", "Giang vien"),
                        "[todo] Danh sach khoa hoc dang co se duoc noi sau.");
                case 2 -> showMessagePlaceholder(
                        "DANG KY KHOA HOC",
                        "[todo] Se chuyen sang man hinh dang ky khoa hoc khi co service.");
                case 3 -> showListPlaceholder(
                        "KHOA HOC DA DANG KY",
                        List.of("ID", "Ten khoa hoc", "Ngay dang ky", "Trang thai"),
                        "[todo] Danh sach khoa hoc da dang ky se duoc noi sau.");
                case 4 -> showMessagePlaceholder(
                        "HUY DANG KY KHOA HOC",
                        "[todo] Chi cho phep huy neu chua duoc xac nhan.");
                case 5 -> showMessagePlaceholder(
                        "CAP NHAT MAT KHAU",
                        "[todo] Se xac thuc email hoac so dien thoai va mat khau cu.");
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }
}
