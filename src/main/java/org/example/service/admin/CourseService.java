package org.example.service.admin;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.entity.Course;
import org.example.entity.CourseTopicSpec;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAllCourses();

    Page<Course> getCourses(PageRequest request);

    Optional<Course> findCourseById(int id);

    Course addCourse(String name, int duration, String instructor);

    default Course addCourse(String name, int duration, String instructor, List<CourseTopicSpec> topics) {
        return addCourse(name, duration, instructor);
    }

    Course updateCourseName(int id, String name);

    Course updateCourseDuration(int id, int duration);

    Course updateCourseInstructor(int id, String instructor);

    Course updateCourseCreatedAt(int id, LocalDate createdAt);

    boolean hasActiveEnrollments(int courseId);

    boolean deleteCourse(int id);

    List<Course> searchCoursesByName(String query);

    Page<Course> searchCoursesByName(String query, PageRequest request);

    List<Course> sortCoursesByName(boolean ascending);

    Page<Course> sortCoursesByName(boolean ascending, PageRequest request);

    List<Course> sortCoursesById(boolean ascending);

    Page<Course> sortCoursesById(boolean ascending, PageRequest request);
}
