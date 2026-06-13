package org.example.presentation.adminmenu;

import org.example.entity.Course;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.admin.CourseService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;
import java.util.Objects;

public final class CourseEditMenuScreen extends AbstractMenuScreen {
    private final CourseService courseService;

    public CourseEditMenuScreen(ConsoleInput input, ConsolePrinter printer, CourseService courseService) {
        super(input, printer);
        this.courseService = Objects.requireNonNull(courseService, "courseService");
    }

    @Override
    public ScreenResult show() {
        while (true) {
            int choice = promptChoice(
                    "CHINH SUA KHOA HOC",
                    List.of(
                            "1. Sua ten khoa hoc",
                            "2. Sua thoi luong (gio)",
                            "3. Sua giang vien",
                            "4. Sua ngay them",
                            "0. Quay lai"),
                    0,
                    4);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.COURSE_MENU;
                }
                case 1 -> handleNameEdit();
                case 2 -> handleDurationEdit();
                case 3 -> handleInstructorEdit();
                case 4 -> handleCreatedAtEdit();
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void handleNameEdit() {
        Integer id = promptPositiveIntOrCancel("Nhap id khoa hoc: ");
        if (id == null) {
            showMessagePlaceholder("CHINH SUA KHOA HOC", "Da huy thao tac chinh sua khoa hoc.");
            return;
        }
        try {
            String newName = promptRequiredLineOrCancel("Nhap ten moi: ");
            if (newName == null) {
                showMessagePlaceholder("CHINH SUA KHOA HOC", "Da huy thao tac chinh sua khoa hoc.");
                return;
            }
            Course updated = courseService.updateCourseName(id, newName);
            showMessagePlaceholder("CHINH SUA KHOA HOC", "Da cap nhat ten khoa hoc thanh: " + updated.getName());
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA KHOA HOC", exception.getMessage());
        }
    }

    private void handleDurationEdit() {
        Integer id = promptPositiveIntOrCancel("Nhap id khoa hoc: ");
        if (id == null) {
            showMessagePlaceholder("CHINH SUA KHOA HOC", "Da huy thao tac chinh sua khoa hoc.");
            return;
        }
        try {
            Integer newDuration = promptPositiveIntOrCancel("Nhap thoi luong moi (gio): ");
            if (newDuration == null) {
                showMessagePlaceholder("CHINH SUA KHOA HOC", "Da huy thao tac chinh sua khoa hoc.");
                return;
            }
            Course updated = courseService.updateCourseDuration(id, newDuration);
            showMessagePlaceholder("CHINH SUA KHOA HOC", "Da cap nhat thoi luong khoa hoc #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA KHOA HOC", exception.getMessage());
        }
    }

    private void handleInstructorEdit() {
        Integer id = promptPositiveIntOrCancel("Nhap id khoa hoc: ");
        if (id == null) {
            showMessagePlaceholder("CHINH SUA KHOA HOC", "Da huy thao tac chinh sua khoa hoc.");
            return;
        }
        try {
            String instructor = promptRequiredLineOrCancel("Nhap giang vien moi: ");
            if (instructor == null) {
                showMessagePlaceholder("CHINH SUA KHOA HOC", "Da huy thao tac chinh sua khoa hoc.");
                return;
            }
            Course updated = courseService.updateCourseInstructor(id, instructor);
            showMessagePlaceholder("CHINH SUA KHOA HOC", "Da cap nhat giang vien cho khoa hoc #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA KHOA HOC", exception.getMessage());
        }
    }

    private void handleCreatedAtEdit() {
        Integer id = promptPositiveIntOrCancel("Nhap id khoa hoc: ");
        if (id == null) {
            showMessagePlaceholder("CHINH SUA KHOA HOC", "Da huy thao tac chinh sua khoa hoc.");
            return;
        }
        try {
            java.time.LocalDate createdAt = promptDateOrCancel("Nhap ngay them moi (yyyy-MM-dd): ");
            if (createdAt == null) {
                showMessagePlaceholder("CHINH SUA KHOA HOC", "Da huy thao tac chinh sua khoa hoc.");
                return;
            }
            Course updated = courseService.updateCourseCreatedAt(id, createdAt);
            showMessagePlaceholder("CHINH SUA KHOA HOC", "Da cap nhat ngay them cho khoa hoc #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA KHOA HOC", exception.getMessage());
        }
    }
}
