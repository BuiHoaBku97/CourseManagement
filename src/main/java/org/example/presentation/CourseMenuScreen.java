package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;

public final class CourseMenuScreen extends AbstractMenuScreen {
    public CourseMenuScreen(ConsoleInput input, ConsolePrinter printer) {
        super(input, printer);
    }

    @Override
    public ScreenResult show() {
        while (true) {
            renderMenu(
                    "QUAN LY KHOA HOC",
                    List.of(
                            "1. Hien thi danh sach khoa hoc",
                            "2. Them moi khoa hoc",
                            "3. Chinh sua thong tin khoa hoc",
                            "4. Xoa khoa hoc theo id",
                            "5. Tim kiem khoa hoc theo ten",
                            "6. Sap xep khoa hoc",
                            "0. Quay lai menu Admin"));

            int choice = input.readChoice("Lua chon: ", 0, 6);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.ADMIN_MENU;
                }
                case 1 -> showListPlaceholder(
                        "DANH SACH KHOA HOC",
                        List.of("ID", "Ten khoa hoc", "Thoi luong", "Giang vien", "Ngay them"),
                        "[todo] danh sach trong");
                case 2 -> showMessagePlaceholder(
                        "THEM MOI KHOA HOC",
                        "[todo] them khoa hoc moi control");
                case 3 -> showMessagePlaceholder(
                        "CHINH SUA THONG TIN KHOA HOC",
                        "[todo] chinh sua khoa hoc control.");
                case 4 -> showMessagePlaceholder(
                        "XOA KHOA HOC THEO ID",
                        "[todo] Se yeu cau xac nhan truoc khi xoa.");
                case 5 -> showListPlaceholder(
                        "TIM KIEM KHOA HOC THEO TEN",
                        List.of("ID", "Ten khoa hoc", "Thoi luong", "Giang vien"),
                        "[todo] Tim kiem tuong doi se duoc gan sau.");
                case 6 -> showMessagePlaceholder(
                        "SAP XEP KHOA HOC",
                        "[todo] Ho tro sap xep theo ten hoac id, tang dan hoac giam dan.");
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }
}
