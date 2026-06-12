package org.example.dao.impl;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.dao.ICourseDao;
import org.example.entity.Course;
import org.example.utils.JdbcConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class CourseDao implements ICourseDao {
    private final JdbcConnectionFactory connectionFactory;

    public CourseDao(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "connectionFactory");
    }

    public List<Course> findAll() {
        return queryCourses("SELECT id, name, duration, instructor, create_at FROM course ORDER BY id ASC");
    }

    public Page<Course> findAll(PageRequest request) {
        List<Course> content =
                queryCoursesPaged(
                        "SELECT id, name, duration, instructor, create_at FROM course ORDER BY id ASC",
                        request);
        return new Page<>(content, request.page(), request.size(), countAll());
    }

    public Optional<Course> findById(int id) {
        List<Course> courses = queryCourses(
                "SELECT id, name, duration, instructor, create_at FROM course WHERE id = ? LIMIT 1",
                id);
        return courses.isEmpty() ? Optional.empty() : Optional.of(courses.get(0));
    }

    public Course insert(String name, int duration, String instructor) {
        String sql =
                "INSERT INTO course (name, duration, instructor) VALUES (?, ?, ?) "
                        + "RETURNING id, name, duration, instructor, create_at";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, duration);
            statement.setString(3, instructor);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalStateException("Khong the tao khoa hoc.");
                }
                int insertedId = resultSet.getInt("id");
                return findById(insertedId)
                        .orElseThrow(() -> new IllegalStateException("Khong the xac minh khoa hoc sau khi tao."));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the tao khoa hoc.", exception);
        }
    }

    public boolean updateName(int id, String name) {
        return updateField("UPDATE course SET name = ? WHERE id = ?", name, id);
    }

    public boolean updateDuration(int id, int duration) {
        return updateField("UPDATE course SET duration = ? WHERE id = ?", duration, id);
    }

    public boolean updateInstructor(int id, String instructor) {
        return updateField("UPDATE course SET instructor = ? WHERE id = ?", instructor, id);
    }

    public boolean updateCreatedAt(int id, Date createdAt) {
        return updateField("UPDATE course SET create_at = ? WHERE id = ?", createdAt, id);
    }

    public boolean deleteById(int id) {
        return updateField("DELETE FROM course WHERE id = ?", id);
    }

    public List<Course> searchByName(String query) {
        return queryCourses(
                "SELECT id, name, duration, instructor, create_at "
                        + "FROM course "
                        + "WHERE LOWER(name) LIKE LOWER(?) "
                        + "ORDER BY id ASC",
                "%" + normalize(query) + "%");
    }

    public long countSearchByName(String query) {
        String sql = "SELECT COUNT(*) AS total FROM course WHERE LOWER(name) LIKE LOWER(?)";
        String pattern = "%" + normalize(query) + "%";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pattern);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong("total") : 0L;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the dem khoa hoc theo tu khoa.", exception);
        }
    }

    public Page<Course> searchByName(String query, PageRequest request) {
        String pattern = "%" + normalize(query) + "%";
        List<Course> content =
                queryCoursesPaged(
                        "SELECT id, name, duration, instructor, create_at "
                                + "FROM course "
                                + "WHERE LOWER(name) LIKE LOWER(?) "
                                + "ORDER BY id ASC",
                        request,
                        pattern);
        return new Page<>(content, request.page(), request.size(), countSearchByName(query));
    }

    public List<Course> sortByName(boolean ascending) {
        return queryCourses(
                "SELECT id, name, duration, instructor, create_at "
                        + "FROM course ORDER BY LOWER(name) "
                        + (ascending ? "ASC" : "DESC"));
    }

    public Page<Course> sortByName(boolean ascending, PageRequest request) {
        List<Course> content =
                queryCoursesPaged(
                        "SELECT id, name, duration, instructor, create_at "
                                + "FROM course ORDER BY LOWER(name) "
                                + (ascending ? "ASC" : "DESC"),
                        request);
        return new Page<>(content, request.page(), request.size(), countAll());
    }

    public List<Course> sortById(boolean ascending) {
        return queryCourses(
                "SELECT id, name, duration, instructor, create_at "
                        + "FROM course ORDER BY id " + (ascending ? "ASC" : "DESC"));
    }

    public Page<Course> sortById(boolean ascending, PageRequest request) {
        List<Course> content =
                queryCoursesPaged(
                        "SELECT id, name, duration, instructor, create_at "
                                + "FROM course ORDER BY id "
                                + (ascending ? "ASC" : "DESC"),
                        request);
        return new Page<>(content, request.page(), request.size(), countAll());
    }

    public long countAll() {
        String sql = "SELECT COUNT(*) AS total FROM course";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getLong("total") : 0L;
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the dem khoa hoc.", exception);
        }
    }

    private boolean updateField(String sql, Object value, int id) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, value);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the cap nhat khoa hoc.", exception);
        }
    }

    private boolean updateField(String sql, int id) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the xoa khoa hoc.", exception);
        }
    }

    private List<Course> queryCourses(String sql, Object... params) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Course> courses = new ArrayList<>();
                while (resultSet.next()) {
                    courses.add(mapCourse(resultSet));
                }
                return courses;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the doc du lieu khoa hoc.", exception);
        }
    }

    private List<Course> queryCoursesPaged(String sql, PageRequest request, Object... params) {
        return queryCourses(sql + " LIMIT ? OFFSET ?", appendPagingParams(params, request));
    }

    private Object[] appendPagingParams(Object[] params, PageRequest request) {
        Object[] pagingParams = new Object[params.length + 2];
        System.arraycopy(params, 0, pagingParams, 0, params.length);
        pagingParams[params.length] = request.size();
        pagingParams[params.length + 1] = request.offset();
        return pagingParams;
    }

    private Course mapCourse(ResultSet resultSet) throws SQLException {
        return new Course(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("duration"),
                resultSet.getString("instructor"),
                resultSet.getDate("create_at").toLocalDate());
    }

    private String normalize(String value) {
        return Objects.requireNonNull(value, "query").trim();
    }
}
