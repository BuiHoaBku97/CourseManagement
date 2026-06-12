package org.example.presentation.adminmenu;

import org.example.entity.Course;

import java.util.List;

final class CourseTableRows {
    static final List<String> HEADERS =
            List.of("ID", "Ten khoa hoc", "Thoi luong", "Giang vien", "Ngay them");

    private CourseTableRows() {
    }

    static List<String> toRow(Course course) {
        return List.of(
                String.valueOf(course.getId()),
                course.getName(),
                String.valueOf(course.getDuration()),
                course.getInstructor(),
                course.getCreatedAt().toString());
    }
}
