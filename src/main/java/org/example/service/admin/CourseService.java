package org.example.service.admin;

import org.example.entity.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAllCourses();

    Optional<Course> findCourseById(int id);

    Course addCourse(String name, int duration, String instructor);

    Course updateCourseName(int id, String name);

    Course updateCourseDuration(int id, int duration);

    Course updateCourseInstructor(int id, String instructor);

    Course updateCourseCreatedAt(int id, LocalDate createdAt);

    boolean deleteCourse(int id);

    List<Course> searchCoursesByName(String query);

    List<Course> sortCoursesByName(boolean ascending);

    List<Course> sortCoursesById(boolean ascending);
}
