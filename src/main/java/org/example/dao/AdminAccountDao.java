package org.example.dao;

import org.example.entity.AdminAccount;
import org.example.utils.JdbcConnectionFactory;
import org.example.utils.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public final class AdminAccountDao {
    private final JdbcConnectionFactory connectionFactory;

    public AdminAccountDao(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "connectionFactory");
    }

    public Optional<AdminAccount> findByUsername(String username) {
        String normalizedUsername = normalize(username);
        String sql = "SELECT id, username, password FROM admin WHERE username = ? LIMIT 1";

        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, normalizedUsername);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(
                        new AdminAccount(
                                resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getString("password")));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the doc du lieu admin.", exception);
        }
    }

    public AdminAccount getDefaultAdmin(){
        return new AdminAccount(0, "admin", PasswordHasher.hash("admin"));
    }

    private String normalize(String value) {
        return Objects.requireNonNull(value, "username").trim();
    }
}
