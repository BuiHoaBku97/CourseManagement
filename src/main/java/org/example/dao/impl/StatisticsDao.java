package org.example.dao.impl;

import org.example.dao.IStatisticsDao;
import org.example.entity.CourseEnrollmentStat;
import org.example.entity.SystemSummary;
import org.example.utils.JdbcConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StatisticsDao implements IStatisticsDao {
    private final JdbcConnectionFactory connectionFactory;

    public StatisticsDao(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "connectionFactory");
    }

    public SystemSummary getSummary() {
        String sql =
                "SELECT "
                        + "(SELECT COUNT(*) FROM course) AS course_count, "
                        + "(SELECT COUNT(*) FROM student) AS student_count";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                return new SystemSummary(0L, 0L);
            }
            return new SystemSummary(resultSet.getLong("course_count"), resultSet.getLong("student_count"));
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the thong ke tong quan.", exception);
        }
    }

    public List<CourseEnrollmentStat> getStudentCountByCourse() {
        String sql =
                "SELECT c.id, c.name, COUNT(e.id) AS student_count "
                        + "FROM course c "
                        + "LEFT JOIN enrollment e ON e.course_id = c.id AND e.status = 'CONFIRM' "
                        + "GROUP BY c.id, c.name "
                        + "ORDER BY c.name ASC";
        return queryStats(sql);
    }

    public List<CourseEnrollmentStat> getTop5CoursesByEnrollment() {
        String sql =
                "SELECT c.id, c.name, COUNT(e.id) AS student_count "
                        + "FROM course c "
                        + "LEFT JOIN enrollment e ON e.course_id = c.id AND e.status = 'CONFIRM' "
                        + "GROUP BY c.id, c.name "
                        + "ORDER BY student_count DESC, c.name ASC "
                        + "LIMIT 5";
        return queryStats(sql);
    }

    public List<CourseEnrollmentStat> getCoursesWithMoreThan10Students() {
        String sql =
                "SELECT c.id, c.name, COUNT(e.id) AS student_count "
                        + "FROM course c "
                        + "LEFT JOIN enrollment e ON e.course_id = c.id AND e.status = 'CONFIRM' "
                        + "GROUP BY c.id, c.name "
                        + "HAVING COUNT(e.id) > 10 "
                        + "ORDER BY student_count DESC, c.name ASC";
        return queryStats(sql);
    }

    private List<CourseEnrollmentStat> queryStats(String sql) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            List<CourseEnrollmentStat> stats = new ArrayList<>();
            while (resultSet.next()) {
                stats.add(
                        new CourseEnrollmentStat(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getLong("student_count")));
            }
            return stats;
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the lay du lieu thong ke.", exception);
        }
    }
}
