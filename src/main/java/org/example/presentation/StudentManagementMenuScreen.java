package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;

public final class StudentManagementMenuScreen extends AbstractMenuScreen {
    public StudentManagementMenuScreen(ConsoleInput input, ConsolePrinter printer) {
        super(input, printer);
    }

    @Override
    public ScreenResult show() {
        while (true) {
            renderMenu(
                    "QUAN LY HOC VIEN",
                    List.of(
                            "1. Hien thi danh sach hoc vien",
                            "2. Them moi hoc vien",
                            "3. Chinh sua thong tin hoc vien",
                            "4. Xoa hoc vien theo id",
                            "5. Tim kiem hoc vien theo ten, email hoac id",
                            "6. Sap xep hoc vien",
                            "0. Quay lai menu Admin"));

            int choice = input.readChoice("Lua chon: ", 0, 6);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.ADMIN_MENU;
                }
                case 1 -> showListPlaceholder(
                        "DANH SACH HOC VIEN",
                        List.of("ID", "Ho ten", "Email", "So dien thoai", "Ngay sinh"),
                        "[todo] Danh sach hoc vien se duoc noi voi service sau.");
                case 2 -> showMessagePlaceholder(
                        "THEM MOI HOC VIEN",
                        "[todo] Man hinh nhap lieu se duoc gan logic them moi sau.");
                case 3 -> showMessagePlaceholder(
                        "CHINH SUA THONG TIN HOC VIEN",
                        "[todo] Se hien thi menu con chon thuoc tinh can sua.");
                case 4 -> showMessagePlaceholder(
                        "XOA HOC VIEN THEO ID",
                        "[todo]Se yeu cau xac nhan truoc khi xoa.");
                case 5 -> showListPlaceholder(
                        "TIM KIEM HOC VIEN",
                        List.of("ID", "Ho ten", "Email", "So dien thoai"),
                        "[todo]Tim kiem tuong doi theo ten, email hoac id.");
                case 6 -> showMessagePlaceholder(
                        "SAP XEP HOC VIEN",
                        "[todo]Ho tro sap xep theo ten hoac id, tang dan hoac giam dan.");
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }
}
