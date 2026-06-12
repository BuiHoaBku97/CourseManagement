package org.example.presentation.studentmenu;

import org.example.entity.Course;

import java.util.List;

final class StudentCourseTableRows {
    static final List<String> HEADERS =
            List.of("ID", "Ten khoa hoc", "Thoi luong", "Giang vien", "Ngay them");

    private StudentCourseTableRows() {
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
