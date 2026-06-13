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

public final class CourseMenuScreen extends AbstractMenuScreen {
    private final CourseService courseService;

    public CourseMenuScreen(ConsoleInput input, ConsolePrinter printer, CourseService courseService) {
        super(input, printer);
        this.courseService = Objects.requireNonNull(courseService, "courseService");
    }

    @Override
    public ScreenResult show() {
        while (true) {
            int choice = promptChoice(
                    "QUAN LY KHOA HOC",
                    List.of(
                            "1. Hien thi danh sach khoa hoc",
                            "2. Them moi khoa hoc",
                            "3. Chinh sua thong tin khoa hoc",
                            "4. Xoa khoa hoc theo id",
                            "5. Tim kiem khoa hoc theo ten",
                            "6. Sap xep khoa hoc",
                            "0. Quay lai menu Admin"),
                    0,
                    6);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.ADMIN_MENU;
                }
                case 1 -> showPagedCourses("DANH SACH KHOA HOC", courseService::getCourses, "Chua co khoa hoc nao.");
                case 2 -> handleAddCourse();
                case 3 -> {
                    return ScreenResult.COURSE_EDIT_MENU;
                }
                case 4 -> handleDeleteCourse();
                case 5 -> handleSearchCourse();
                case 6 -> {
                    return ScreenResult.COURSE_SORT_MENU;
                }
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void handleAddCourse() {
        try {
            String name = promptRequiredLineOrCancel("Nhap ten khoa hoc: ");
            if (name == null) {
                showMessagePlaceholder("THEM MOI KHOA HOC", "Da huy thao tac them khoa hoc.");
                return;
            }
            Integer duration = promptPositiveIntOrCancel("Nhap thoi luong (so gio): ");
            if (duration == null) {
                showMessagePlaceholder("THEM MOI KHOA HOC", "Da huy thao tac them khoa hoc.");
                return;
            }
            String instructor = promptRequiredLineOrCancel("Nhap giang vien: ");
            if (instructor == null) {
                showMessagePlaceholder("THEM MOI KHOA HOC", "Da huy thao tac them khoa hoc.");
                return;
            }
            Course course = courseService.addCourse(name, duration, instructor);
            showMessagePlaceholder(
                    "THEM MOI KHOA HOC",
                    "Da tao khoa hoc #" + course.getId() + " - " + course.getName() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("THEM MOI KHOA HOC", exception.getMessage());
        }
    }

    private void handleDeleteCourse() {
        Integer id = promptPositiveIntOrCancel("Nhap id khoa hoc can xoa: ");
        if (id == null) {
            showMessagePlaceholder("XOA KHOA HOC THEO ID", "Da huy thao tac xoa khoa hoc.");
            return;
        }
        try {
            Course course = courseService.findCourseById(id)
                    .orElseThrow(() -> new IllegalStateException("Khong tim thay khoa hoc voi id " + id + "."));
            if (!confirmAction("XAC NHAN XOA KHOA HOC " + course.getName())) {
                showMessagePlaceholder("XOA KHOA HOC THEO ID", "Da huy thao tac xoa khoa hoc.");
                return;
            }
            courseService.deleteCourse(id);
            showMessagePlaceholder("XOA KHOA HOC THEO ID", "Da xoa khoa hoc #" + id + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("XOA KHOA HOC THEO ID", exception.getMessage());
        }
    }

    private void handleSearchCourse() {
        try {
            String query = promptRequiredLineOrCancel("Nhap tu khoa tim kiem: ");
            if (query == null) {
                showMessagePlaceholder("TIM KIEM KHOA HOC THEO TEN", "Da huy thao tac tim kiem khoa hoc.");
                return;
            }
            showPagedCourses(
                    "TIM KIEM KHOA HOC THEO TEN",
                    request -> courseService.searchCoursesByName(query, request),
                    "Khong tim thay khoa hoc phu hop.");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("TIM KIEM KHOA HOC THEO TEN", exception.getMessage());
        }
    }

    private void showPagedCourses(
            String title, Function<PageRequest, Page<Course>> pageLoader, String emptyMessage) {
        showPagedTable(title, CourseTableRows.HEADERS, emptyMessage, pageLoader, CourseTableRows::toRow);
    }
}
