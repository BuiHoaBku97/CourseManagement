package org.example.presentation.studentmenu;

import org.example.entity.Course;
import org.example.entity.CourseRecommendation;

import java.util.List;

final class StudentCourseTableRows {
    static final List<String> HEADERS =
            List.of("ID", "Ten khoa hoc", "Thoi luong", "Giang vien", "Ngay them", "De xuat");

    private StudentCourseTableRows() {
    }

    static List<String> toRow(Course course) {
        return List.of(
                String.valueOf(course.getId()),
                course.getName(),
                String.valueOf(course.getDuration()),
                course.getInstructor(),
                course.getCreatedAt().toString(),
                "");
    }

    static List<String> toRow(CourseRecommendation recommendation) {
        Course course = recommendation.getCourse();
        return List.of(
                String.valueOf(course.getId()),
                course.getName(),
                String.valueOf(course.getDuration()),
                course.getInstructor(),
                course.getCreatedAt().toString(),
                recommendation.getLabel());
    }
}
