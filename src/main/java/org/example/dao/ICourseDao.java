package org.example.dao;

import org.example.entity.Course;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ICourseDao {
    List<Course> findAll();

    Optional<Course> findById(int id);

    Course insert(String name, int duration, String instructor);

    boolean updateName(int id, String name);

    boolean updateDuration(int id, int duration);

    boolean updateInstructor(int id, String instructor);

    boolean updateCreatedAt(int id, Date createdAt);

    boolean deleteById(int id);

    List<Course> searchByName(String query);

    List<Course> sortByName(boolean ascending);

    List<Course> sortById(boolean ascending);

    long countAll();
}
