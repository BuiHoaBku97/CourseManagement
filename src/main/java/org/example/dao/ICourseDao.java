package org.example.dao;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.entity.Course;
import org.example.entity.CourseTopicSpec;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICourseDao {
    List<Course> findAll();

    Page<Course> findAll(PageRequest request);

    Optional<Course> findById(int id);

    Course insert(String name, int duration, String instructor);

    default Course insert(String name, int duration, String instructor, List<CourseTopicSpec> topicSpecs) {
        return insert(name, duration, instructor);
    }

    Map<Integer, List<CourseTopicSpec>> findTopicsByCourseIds(List<Integer> courseIds);

    boolean updateName(int id, String name);

    boolean updateDuration(int id, int duration);

    boolean updateInstructor(int id, String instructor);

    boolean updateCreatedAt(int id, Date createdAt);

    boolean deleteById(int id);

    List<Course> searchByName(String query);

    long countSearchByName(String query);

    Page<Course> searchByName(String query, PageRequest request);

    List<Course> sortByName(boolean ascending);

    Page<Course> sortByName(boolean ascending, PageRequest request);

    List<Course> sortById(boolean ascending);

    Page<Course> sortById(boolean ascending, PageRequest request);

    long countAll();
}
