package org.example.dao;

import org.example.entity.StudentAccount;
import org.example.utils.JdbcConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public final class StudentAccountDao {
    private final JdbcConnectionFactory connectionFactory;

    public StudentAccountDao(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "connectionFactory");
    }

    public Optional<StudentAccount> findByLoginId(String loginId) {
        String normalizedLoginId = normalize(loginId);
        String sql =
                "SELECT id, name, email, phone, password "
                        + "FROM student "
                        + "WHERE LOWER(email) = LOWER(?) OR phone = ? "
                        + "LIMIT 1";

        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, normalizedLoginId);
            statement.setString(2, normalizedLoginId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(
                        new StudentAccount(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("phone"),
                                resultSet.getString("password")));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the doc du lieu hoc vien.", exception);
        }
    }

    private String normalize(String value) {
        return Objects.requireNonNull(value, "loginId").trim();
    }
}
