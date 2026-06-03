package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;

public final class EnrollmentMenuScreen extends AbstractMenuScreen {
    public EnrollmentMenuScreen(ConsoleInput input, ConsolePrinter printer) {
        super(input, printer);
    }

    @Override
    public ScreenResult show() {
        while (true) {
            renderMenu(
                    "QUAN LY DANG KY KHOA HOC",
                    List.of(
                            "1. Hien thi danh sach sinh vien theo tung khoa hoc",
                            "2. Them sinh vien dang ky khoa hoc",
                            "3. Xoa sinh vien khoi khoa hoc",
                            "0. Quay lai menu Admin"));

            int choice = input.readChoice("Lua chon: ", 0, 3);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.ADMIN_MENU;
                }
                case 1 -> showListPlaceholder(
                        "DANH SACH DANG KY",
                        List.of("Khoa hoc", "Hoc vien", "Trang thai", "Ngay dang ky"),
                        "[todo] Danh sach dang ky theo tung khoa hoc se duoc noi sau.");
                case 2 -> showMessagePlaceholder(
                        "DUYET SINH VIEN DANG KY",
                        "[todo] Man hinh xac nhan phe duyet se duoc gan sau.");
                case 3 -> showMessagePlaceholder(
                        "XOA SINH VIEN KHOI KHOA HOC",
                        "[todo] Se yeu cau xac nhan truoc khi xoa.");
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }
}
