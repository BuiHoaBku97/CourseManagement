package org.example.dao.impl;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.dao.IEnrollmentDao;
import org.example.entity.Enrollment;
import org.example.entity.EnrollmentDetail;
import org.example.entity.EnrollmentStatus;
import org.example.utils.JdbcConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class EnrollmentDao implements IEnrollmentDao {
    private final JdbcConnectionFactory connectionFactory;

    public EnrollmentDao(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "connectionFactory");
    }

    public List<EnrollmentDetail> findAllDetails() {
        String sql =
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "ORDER BY c.name ASC, e.registered_at DESC, e.id ASC";
        return queryDetails(sql);
    }

    public Page<EnrollmentDetail> findAllDetails(PageRequest request) {
        String sql =
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "ORDER BY c.name ASC, e.registered_at DESC, e.id ASC";
        List<EnrollmentDetail> content = queryDetailsPaged(sql, request);
        return new Page<>(content, request.page(), request.size(), countAllDetails());
    }

    public List<EnrollmentDetail> findWaitingDetails() {
        String sql =
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "WHERE e.status = 'WAITING' "
                        + "ORDER BY e.registered_at ASC, e.id ASC";
        return queryDetails(sql);
    }

    public Page<EnrollmentDetail> findWaitingDetails(PageRequest request) {
        String sql =
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "WHERE e.status = 'WAITING' "
                        + "ORDER BY e.registered_at ASC, e.id ASC";
        List<EnrollmentDetail> content = queryDetailsPaged(sql, request);
        return new Page<>(content, request.page(), request.size(), countWaitingDetails());
    }

    public List<EnrollmentDetail> findByStudentId(int studentId) {
        return queryDetailsByStudent(
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "WHERE e.student_id = ? "
                        + "ORDER BY c.name ASC, e.id ASC",
                studentId);
    }

    public Page<EnrollmentDetail> findByStudentId(int studentId, PageRequest request) {
        String sql =
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "WHERE e.student_id = ? "
                        + "ORDER BY c.name ASC, e.id ASC";
        List<EnrollmentDetail> content = queryDetailsByStudentPaged(sql, studentId, request);
        return new Page<>(content, request.page(), request.size(), countByStudentId(studentId));
    }

    public List<EnrollmentDetail> findByStudentIdSortedByCourseName(int studentId, boolean ascending) {
        return queryDetailsByStudent(
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "WHERE e.student_id = ? "
                        + "ORDER BY c.name "
                        + (ascending ? "ASC" : "DESC"),
                studentId);
    }

    public Page<EnrollmentDetail> findByStudentIdSortedByCourseName(int studentId, boolean ascending, PageRequest request) {
        String sql =
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "WHERE e.student_id = ? "
                        + "ORDER BY c.name "
                        + (ascending ? "ASC" : "DESC");
        List<EnrollmentDetail> content = queryDetailsByStudentPaged(sql, studentId, request);
        return new Page<>(content, request.page(), request.size(), countByStudentId(studentId));
    }

    public List<EnrollmentDetail> findByStudentIdSortedByCourseId(int studentId, boolean ascending) {
        return queryDetailsByStudent(
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "WHERE e.student_id = ? "
                        + "ORDER BY c.id "
                        + (ascending ? "ASC" : "DESC"),
                studentId);
    }

    public Page<EnrollmentDetail> findByStudentIdSortedByCourseId(int studentId, boolean ascending, PageRequest request) {
        String sql =
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "WHERE e.student_id = ? "
                        + "ORDER BY c.id "
                        + (ascending ? "ASC" : "DESC");
        List<EnrollmentDetail> content = queryDetailsByStudentPaged(sql, studentId, request);
        return new Page<>(content, request.page(), request.size(), countByStudentId(studentId));
    }

    public Optional<EnrollmentDetail> findDetailByStudentAndCourse(int studentId, int courseId) {
        String sql =
                "SELECT e.id, e.course_id, c.name AS course_name, e.student_id, s.name AS student_name, "
                        + "e.status, e.registered_at "
                        + "FROM enrollment e "
                        + "JOIN course c ON c.id = e.course_id "
                        + "JOIN student s ON s.id = e.student_id "
                        + "WHERE e.student_id = ? AND e.course_id = ? "
                        + "LIMIT 1";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapDetail(resultSet));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the doc du lieu dang ky.", exception);
        }
    }

    public EnrollmentDetail insertWaiting(int studentId, int courseId) {
        String sql =
                "INSERT INTO enrollment (student_id, course_id, status) "
                        + "VALUES (?, ?, 'WAITING') "
                        + "RETURNING id, student_id, course_id, registered_at, status";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalStateException("Khong the tao dang ky.");
                }
                return findDetailByStudentAndCourse(studentId, courseId)
                        .orElseThrow(() -> new IllegalStateException("Khong the xac minh dang ky sau khi tao."));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the tao dang ky.", exception);
        }
    }

    public boolean updateStatusByStudentAndCourse(int studentId, int courseId, EnrollmentStatus status) {
        String sql = "UPDATE enrollment SET status = CAST(? AS enrollment_status) WHERE student_id = ? AND course_id = ?";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, studentId);
            statement.setInt(3, courseId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the cap nhat trang thai dang ky.", exception);
        }
    }

    public Optional<Enrollment> findById(int id) {
        String sql =
                "SELECT id, student_id, course_id, registered_at, status "
                        + "FROM enrollment WHERE id = ? LIMIT 1";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapEnrollment(resultSet));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the doc du lieu dang ky.", exception);
        }
    }

    public boolean approve(int id) {
        return updateStatus(id, "CONFIRM");
    }

    public boolean cancel(int id) {
        return updateStatus(id, "CANCEL");
    }

    private boolean updateStatus(int id, String status) {
        String sql = "UPDATE enrollment SET status = CAST(? AS enrollment_status) WHERE id = ?";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the cap nhat trang thai dang ky.", exception);
        }
    }

    private List<EnrollmentDetail> queryDetails(String sql, Object... params) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                List<EnrollmentDetail> details = new ArrayList<>();
                while (resultSet.next()) {
                    details.add(mapDetail(resultSet));
                }
                return details;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the doc du lieu dang ky.", exception);
        }
    }

    private List<EnrollmentDetail> queryDetailsPaged(String sql, PageRequest request) {
        return queryDetails(sql + " LIMIT ? OFFSET ?", request.size(), request.offset());
    }

    private List<EnrollmentDetail> queryDetailsByStudent(String sql, int studentId) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<EnrollmentDetail> details = new ArrayList<>();
                while (resultSet.next()) {
                    details.add(mapDetail(resultSet));
                }
                return details;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the doc du lieu dang ky.", exception);
        }
    }

    private List<EnrollmentDetail> queryDetailsByStudentPaged(String sql, int studentId, PageRequest request) {
        return queryDetailsByStudent(sql + " LIMIT ? OFFSET ?", studentId, request.size(), request.offset());
    }

    private List<EnrollmentDetail> queryDetailsByStudent(String sql, int studentId, Object... extraParams) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            for (int i = 0; i < extraParams.length; i++) {
                statement.setObject(i + 2, extraParams[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                List<EnrollmentDetail> details = new ArrayList<>();
                while (resultSet.next()) {
                    details.add(mapDetail(resultSet));
                }
                return details;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the doc du lieu dang ky.", exception);
        }
    }

    private long countAllDetails() {
        String sql = "SELECT COUNT(*) AS total FROM enrollment";
        return countDetails(sql);
    }

    private long countWaitingDetails() {
        String sql = "SELECT COUNT(*) AS total FROM enrollment WHERE status = 'WAITING'";
        return countDetails(sql);
    }

    private long countByStudentId(int studentId) {
        String sql = "SELECT COUNT(*) AS total FROM enrollment WHERE student_id = ?";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong("total") : 0L;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the dem dang ky.", exception);
        }
    }

    private long countDetails(String sql) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getLong("total") : 0L;
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the dem dang ky.", exception);
        }
    }

    private EnrollmentDetail mapDetail(ResultSet resultSet) throws SQLException {
        return new EnrollmentDetail(
                resultSet.getInt("id"),
                resultSet.getInt("course_id"),
                resultSet.getString("course_name"),
                resultSet.getInt("student_id"),
                resultSet.getString("student_name"),
                EnrollmentStatus.valueOf(resultSet.getString("status")),
                resultSet.getTimestamp("registered_at").toLocalDateTime());
    }

    private Enrollment mapEnrollment(ResultSet resultSet) throws SQLException {
        return new Enrollment(
                resultSet.getInt("id"),
                resultSet.getInt("student_id"),
                resultSet.getInt("course_id"),
                resultSet.getTimestamp("registered_at").toLocalDateTime(),
                EnrollmentStatus.valueOf(resultSet.getString("status")));
    }
}
