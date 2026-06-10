package org.example.service.admin;

import org.example.dao.CourseDao;
import org.example.entity.Course;
import org.example.utils.InputValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JdbcCourseService implements CourseService {
    private final CourseDao courseDao;

    public JdbcCourseService(CourseDao courseDao) {
        this.courseDao = Objects.requireNonNull(courseDao, "courseDao");
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.findAll();
    }

    @Override
    public Optional<Course> findCourseById(int id) {
        return courseDao.findById(id);
    }

    @Override
    public Course addCourse(String name, int duration, String instructor) {
        validateCourse(name, duration, instructor);
        return courseDao.insert(name.trim(), duration, instructor.trim());
    }

    @Override
    public Course updateCourseName(int id, String name) {
        validateText(name, "ten khoa hoc");
        ensureCourseExists(id);
        if (!courseDao.updateName(id, name.trim())) {
            throw new IllegalStateException("Khong the cap nhat ten khoa hoc.");
        }
        return requireCourse(id);
    }

    @Override
    public Course updateCourseDuration(int id, int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Thoi luong phai lon hon 0.");
        }
        ensureCourseExists(id);
        if (!courseDao.updateDuration(id, duration)) {
            throw new IllegalStateException("Khong the cap nhat thoi luong khoa hoc.");
        }
        return requireCourse(id);
    }

    @Override
    public Course updateCourseInstructor(int id, String instructor) {
        validateText(instructor, "giang vien");
        ensureCourseExists(id);
        if (!courseDao.updateInstructor(id, instructor.trim())) {
            throw new IllegalStateException("Khong the cap nhat giang vien.");
        }
        return requireCourse(id);
    }

    @Override
    public Course updateCourseCreatedAt(int id, LocalDate createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("Ngay them khong duoc de trong.");
        }
        ensureCourseExists(id);
        if (!courseDao.updateCreatedAt(id, java.sql.Date.valueOf(createdAt))) {
            throw new IllegalStateException("Khong the cap nhat ngay them.");
        }
        return requireCourse(id);
    }

    @Override
    public boolean deleteCourse(int id) {
        ensureCourseExists(id);
        return courseDao.deleteById(id);
    }

    @Override
    public List<Course> searchCoursesByName(String query) {
        validateText(query, "tu khoa tim kiem");
        return courseDao.searchByName(query.trim());
    }

    @Override
    public List<Course> sortCoursesByName(boolean ascending) {
        return courseDao.sortByName(ascending);
    }

    @Override
    public List<Course> sortCoursesById(boolean ascending) {
        return courseDao.sortById(ascending);
    }

    private void validateCourse(String name, int duration, String instructor) {
        validateText(name, "ten khoa hoc");
        if (duration <= 0) {
            throw new IllegalArgumentException("Thoi luong phai lon hon 0.");
        }
        validateText(instructor, "giang vien");
    }

    private void validateText(String value, String fieldName) {
        if (InputValidator.isBlank(value)) {
            throw new IllegalArgumentException("Truong " + fieldName + " khong duoc de trong.");
        }
    }

    private void ensureCourseExists(int id) {
        if (courseDao.findById(id).isEmpty()) {
            throw new IllegalStateException("Khong tim thay khoa hoc voi id " + id + ".");
        }
    }

    private Course requireCourse(int id) {
        return courseDao.findById(id)
                .orElseThrow(() -> new IllegalStateException("Khong tim thay khoa hoc voi id " + id + "."));
    }
}
