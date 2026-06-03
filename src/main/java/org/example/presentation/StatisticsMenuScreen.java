package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;

public final class StatisticsMenuScreen extends AbstractMenuScreen {
    public StatisticsMenuScreen(ConsoleInput input, ConsolePrinter printer) {
        super(input, printer);
    }

    @Override
    public ScreenResult show() {
        while (true) {
            renderMenu(
                    "THONG KE",
                    List.of(
                            "1. Tong so khoa hoc va tong so hoc vien",
                            "2. Tong so hoc vien theo tung khoa",
                            "3. Top 5 khoa hoc dong sinh vien nhat",
                            "4. Liet ke khoa hoc co tren 10 hoc vien",
                            "0. Quay lai menu Admin"));

            int choice = input.readChoice("Lua chon: ", 0, 4);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.ADMIN_MENU;
                }
                case 1 -> showMessagePlaceholder(
                        "TONG QUAN HE THONG",
                        "[todo]Se hien thi cac chi so tong hop cho khoa hoc va hoc vien.");
                case 2 -> showListPlaceholder(
                        "TONG SO HOC VIEN THEO TUNG KHOA",
                        List.of("Khoa hoc", "So hoc vien"),
                        "[todo]Bao cao tong hop theo tung khoa hoc se duoc noi sau.");
                case 3 -> showListPlaceholder(
                        "TOP 5 KHOA HOC DONG SINH VIEN NHAT",
                        List.of("Xep hang", "Khoa hoc", "So hoc vien"),
                        "[todo]Danh sach top 5 se duoc noi voi service sau.");
                case 4 -> showListPlaceholder(
                        "KHOA HOC CO TREN 10 HOC VIEN",
                        List.of("ID", "Ten khoa hoc", "So hoc vien"),
                        "[todo]Danh sach loc theo dieu kien se duoc noi sau.");
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }
}
