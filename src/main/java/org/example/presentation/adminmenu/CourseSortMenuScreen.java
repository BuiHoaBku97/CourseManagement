package org.example.presentation.adminmenu;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.entity.Course;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.admin.CourseService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
                case 1 -> showPagedCourses(
                        "SAP XEP KHOA HOC - TEN TANG DAN",
                        request -> courseService.sortCoursesByName(true, request));
                case 2 -> showPagedCourses(
                        "SAP XEP KHOA HOC - TEN GIAM DAN",
                        request -> courseService.sortCoursesByName(false, request));
                case 3 -> showPagedCourses(
                        "SAP XEP KHOA HOC - ID TANG DAN",
                        request -> courseService.sortCoursesById(true, request));
                case 4 -> showPagedCourses(
                        "SAP XEP KHOA HOC - ID GIAM DAN",
                        request -> courseService.sortCoursesById(false, request));
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void showPagedCourses(String title, Function<PageRequest, Page<Course>> pageLoader) {
        showPagedTable(title, CourseTableRows.HEADERS, "Chua co du lieu", pageLoader, CourseTableRows::toRow);
    }
}
