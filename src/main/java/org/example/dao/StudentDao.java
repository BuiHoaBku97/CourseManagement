package org.example.dao;

import org.example.entity.Student;
import org.example.utils.JdbcConnectionFactory;
import org.example.utils.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class StudentDao {
    private final JdbcConnectionFactory connectionFactory;

    public StudentDao(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "connectionFactory");
    }

    public List<Student> findAll() {
        return queryStudents(
                "SELECT id, name, dob, sex, email, phone, password, create_at "
                        + "FROM student ORDER BY id ASC");
    }

    public Optional<Student> findById(int id) {
        List<Student> students = queryStudents(
                "SELECT id, name, dob, sex, email, phone, password, create_at "
                        + "FROM student WHERE id = ? LIMIT 1",
                id);
        return students.isEmpty() ? Optional.empty() : Optional.of(students.get(0));
    }

    public Student insert(Student student) {
        String sql =
                "INSERT INTO student (name, dob, sex, email, phone, password) "
                        + "VALUES (?, ?, ?, ?, ?, ?) "
                        + "RETURNING id, name, dob, sex, email, phone, password, create_at";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            bindStudentForInsert(statement, student);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalStateException("Khong the tao hoc vien.");
                }
                return mapStudent(resultSet);
            }
        } catch (SQLException exception) {
            throw translate(exception, "Khong the tao hoc vien.");
        }
    }

    public boolean updateName(int id, String name) {
        return updateField("UPDATE student SET name = ? WHERE id = ?", name, id);
    }

    public boolean updateDob(int id, java.time.LocalDate dob) {
        return updateField("UPDATE student SET dob = ? WHERE id = ?", java.sql.Date.valueOf(dob), id);
    }

    public boolean updateSex(int id, boolean sex) {
        return updateField("UPDATE student SET sex = ? WHERE id = ?", sex ? "1" : "0", id);
    }

    public boolean updateEmail(int id, String email) {
        return updateField("UPDATE student SET email = ? WHERE id = ?", email, id);
    }

    public boolean updatePhone(int id, String phone) {
        return updateField("UPDATE student SET phone = ? WHERE id = ?", phone, id);
    }

    public boolean updatePassword(int id, String rawPassword) {
        return updateField("UPDATE student SET password = ? WHERE id = ?", PasswordHasher.hash(rawPassword), id);
    }

    public boolean deleteById(int id) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM student WHERE id = ?")) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the xoa hoc vien.", exception);
        }
    }

    public List<Student> search(String query) {
        String normalized = normalize(query);
        return queryStudents(
                "SELECT id, name, dob, sex, email, phone, password, create_at "
                        + "FROM student "
                        + "WHERE LOWER(name) LIKE LOWER(?) "
                        + "OR LOWER(email) LIKE LOWER(?) "
                        + "OR CAST(id AS TEXT) LIKE ? "
                        + "ORDER BY id ASC",
                "%" + normalized + "%",
                "%" + normalized + "%",
                "%" + normalized + "%");
    }

    public List<Student> sortByName(boolean ascending) {
        return queryStudents(
                "SELECT id, name, dob, sex, email, phone, password, create_at "
                        + "FROM student ORDER BY LOWER(name) "
                        + (ascending ? "ASC" : "DESC"));
    }

    public List<Student> sortById(boolean ascending) {
        return queryStudents(
                "SELECT id, name, dob, sex, email, phone, password, create_at "
                        + "FROM student ORDER BY id " + (ascending ? "ASC" : "DESC"));
    }

    public long countAll() {
        String sql = "SELECT COUNT(*) AS total FROM student";
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getLong("total") : 0L;
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the dem hoc vien.", exception);
        }
    }

    private void bindStudentForInsert(PreparedStatement statement, Student student)
            throws SQLException {
        statement.setString(1, student.getName());
        statement.setDate(2, java.sql.Date.valueOf(student.getDob()));
        statement.setString(3, student.isSex() ? "1" : "0");
        statement.setString(4, student.getEmail());
        statement.setString(5, student.getPhone());
        statement.setString(6, student.getPasswordHash());
    }

    private boolean updateField(String sql, Object value, int id) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, value);
            statement.setInt(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw translate(exception, "Khong the cap nhat hoc vien.");
        }
    }

    private List<Student> queryStudents(String sql, Object... params) {
        try (Connection connection = connectionFactory.openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Student> students = new ArrayList<>();
                while (resultSet.next()) {
                    students.add(mapStudent(resultSet));
                }
                return students;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the doc du lieu hoc vien.", exception);
        }
    }

    private Student mapStudent(ResultSet resultSet) throws SQLException {
        return new Student(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDate("dob").toLocalDate(),
                resultSet.getBoolean("sex"),
                resultSet.getString("email"),
                resultSet.getString("phone"),
                resultSet.getString("password"),
                resultSet.getDate("create_at").toLocalDate());
    }

    private IllegalStateException translate(SQLException exception, String fallbackMessage) {
        if ("23505".equals(exception.getSQLState())) {
            return new IllegalStateException("Du lieu bi trung. Vui long kiem tra lai email hoac thong tin lien quan.", exception);
        }
        return new IllegalStateException(fallbackMessage, exception);
    }

    private String normalize(String value) {
        return Objects.requireNonNull(value, "query").trim();
    }
}
