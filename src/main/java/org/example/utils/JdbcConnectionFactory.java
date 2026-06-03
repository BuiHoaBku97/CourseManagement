package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JdbcConnectionFactory {
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/course_management";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "postgres";

    private final String url;
    private final String username;
    private final String password;

    public JdbcConnectionFactory() {
        this(
                readSetting("course.db.url", "COURSE_DB_URL", DEFAULT_URL),
                readSetting("course.db.user", "COURSE_DB_USER", DEFAULT_USER),
                readSetting("course.db.password", "COURSE_DB_PASSWORD", DEFAULT_PASSWORD));
    }

    public JdbcConnectionFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    private static String readSetting(String systemProperty, String envKey, String defaultValue) {
        String systemValue = System.getProperty(systemProperty);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }

        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        return defaultValue;
    }
}
