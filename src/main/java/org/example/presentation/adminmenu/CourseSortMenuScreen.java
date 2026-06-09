package org.example.presentation.adminmenu;

import org.example.entity.Course;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.CourseService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CourseSortMenuScreen extends AbstractMenuScreen {
    private final CourseService courseService;

    public CourseSortMenuScreen(ConsoleInput input, ConsolePrinter printer, CourseService courseService) {
        super(input, printer);
        this.courseService = Objects.requireNonNull(courseService, "courseService");
    }

    @Override
    public ScreenResult show() {
        while (true) {
            int choice = promptChoice(
                    "SAP XEP KHOA HOC",
                    List.of(
                            "1. Sap xep theo ten tang dan",
                            "2. Sap xep theo ten giam dan",
                            "3. Sap xep theo id tang dan",
                            "4. Sap xep theo id giam dan",
                            "0. Quay lai"),
                    0,
                    4);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.COURSE_MENU;
                }
                case 1 -> showCourses("SAP XEP KHOA HOC - TEN TANG DAN", courseService.sortCoursesByName(true));
                case 2 -> showCourses("SAP XEP KHOA HOC - TEN GIAM DAN", courseService.sortCoursesByName(false));
                case 3 -> showCourses("SAP XEP KHOA HOC - ID TANG DAN", courseService.sortCoursesById(true));
                case 4 -> showCourses("SAP XEP KHOA HOC - ID GIAM DAN", courseService.sortCoursesById(false));
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void showCourses(String title, List<Course> courses) {
        List<List<String>> rows = new ArrayList<>();
        for (Course course : courses) {
            rows.add(
                    List.of(
                            String.valueOf(course.getId()),
                            course.getName(),
                            String.valueOf(course.getDuration()),
                            course.getInstructor(),
                            course.getCreatedAt().toString()));
        }
        if (rows.isEmpty()) {
            rows.add(List.of("Chua co du lieu", "", "", "", ""));
        }
        showTable(title, List.of("ID", "Ten khoa hoc", "Thoi luong", "Giang vien", "Ngay them"), rows);
    }
}
