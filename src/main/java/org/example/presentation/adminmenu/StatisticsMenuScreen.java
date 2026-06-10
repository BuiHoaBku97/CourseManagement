package org.example.presentation.adminmenu;

import org.example.entity.CourseEnrollmentStat;
import org.example.entity.SystemSummary;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.admin.StatisticsService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StatisticsMenuScreen extends AbstractMenuScreen {
    private final StatisticsService statisticsService;

    public StatisticsMenuScreen(ConsoleInput input, ConsolePrinter printer, StatisticsService statisticsService) {
        super(input, printer);
        this.statisticsService = Objects.requireNonNull(statisticsService, "statisticsService");
    }

    @Override
    public ScreenResult show() {
        while (true) {
            int choice = promptChoice(
                    "THONG KE",
                    List.of(
                            "1. Tong so luong khoa hoc va tong so hoc vien",
                            "2. Tong so hoc vien theo tung khoa",
                            "3. Top 5 khoa hoc dong sinh vien nhat",
                            "4. Liet ke cac khoa hoc co tren 10 hoc vien",
                            "0. Quay lai menu Admin"),
                    0,
                    4);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.ADMIN_MENU;
                }
                case 1 -> showSummary();
                case 2 -> showStats("TONG SO HOC VIEN THEO TUNG KHOA", statisticsService.getStudentCountByCourse());
                case 3 -> showStats("TOP 5 KHOA HOC DONG SINH VIEN NHAT", statisticsService.getTop5CoursesByEnrollment());
                case 4 -> showStats("KHOA HOC CO TREN 10 HOC VIEN", statisticsService.getCoursesWithMoreThan10Students());
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void showSummary() {
        SystemSummary summary = statisticsService.getSummary();
        showMessagePlaceholder(
                "TONG QUAN HE THONG",
                "Tong so khoa hoc: "
                        + summary.getCourseCount()
                        + ", tong so hoc vien: "
                        + summary.getStudentCount()
                        + ".");
    }

    private void showStats(String title, List<CourseEnrollmentStat> stats) {
        List<List<String>> rows = new ArrayList<>();
        for (CourseEnrollmentStat stat : stats) {
            rows.add(
                    List.of(
                            String.valueOf(stat.getCourseId()),
                            stat.getCourseName(),
                            String.valueOf(stat.getStudentCount())));
        }
        if (rows.isEmpty()) {
            rows.add(List.of("Chua co du lieu", "", ""));
        }
        showTable(title, List.of("ID", "Ten khoa hoc", "So hoc vien"), rows);
    }
}
