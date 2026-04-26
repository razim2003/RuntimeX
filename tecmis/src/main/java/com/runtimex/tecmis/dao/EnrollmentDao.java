package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.EnrollmentRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {

    private final Connection connection;

    public EnrollmentDao(Connection connection) {
        this.connection = connection;
    }

    public List<EnrollmentRecord> findEnrollments(String keyword) {
        List<EnrollmentRecord> rows = new ArrayList<>();
        String sql = """
                SELECT u.id AS student_id,
                       CONCAT(u.f_name, ' ', u.l_name) AS student_name,
                       c.course_code,
                       c.title AS course_title
                  FROM enrollment e
                  JOIN users u ON u.id = e.stu_id
                  JOIN course_unit c ON c.course_code = e.course_code
                 WHERE (? = ''
                        OR u.id LIKE CONCAT('%', ?, '%')
                        OR u.f_name LIKE CONCAT('%', ?, '%')
                        OR u.l_name LIKE CONCAT('%', ?, '%')
                        OR c.course_code LIKE CONCAT('%', ?, '%')
                        OR c.title LIKE CONCAT('%', ?, '%'))
                 ORDER BY u.id, c.course_code
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);
            ps.setString(5, keyword);
            ps.setString(6, keyword);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new EnrollmentRecord(
                            rs.getString("student_id"),
                            rs.getString("student_name"),
                            rs.getString("course_code"),
                            rs.getString("course_title")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading enrollments", e);
        }
        return rows;
    }

    public void enrollStudent(String studentId, String courseCode) {
        String sql = "INSERT INTO enrollment (stu_id, course_code) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseCode);
            ps.executeUpdate();
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                throw new RuntimeException("Student already enrolled in this course");
            }
            throw new RuntimeException("Error enrolling student", e);
        }
    }

    public void removeEnrollment(String studentId, String courseCode) {
        String sql = "DELETE FROM enrollment WHERE stu_id = ? AND course_code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseCode);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Enrollment not found for removal");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error removing enrollment", e);
        }
    }
}
