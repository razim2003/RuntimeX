package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.CourseUnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseUnitDAO {

    private final Connection connection;

    public CourseUnitDAO(Connection connection) {
        this.connection = connection;
    }


    public void addCourse(CourseUnit course) {
        String sql = "INSERT INTO course_unit (course_code, title, credit) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, course.getCourseCode());
            ps.setString(2, course.getTitle());
            ps.setInt(3, course.getCredit());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // 🔧 show real error
            throw new RuntimeException("Error adding course");
        }
    }


    public CourseUnit getCourseById(String code) {
        String sql = "SELECT * FROM course_unit WHERE course_code = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) { // 🔧 properly closed
                if (rs.next()) {
                    return new CourseUnit(
                            rs.getString("course_code"),
                            rs.getString("title"),
                            rs.getInt("credit")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching course");
        }

        return null;
    }


    public List<CourseUnit> getAllCourses() {
        List<CourseUnit> list = new ArrayList<>();
        String sql = "SELECT * FROM course_unit";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new CourseUnit(
                        rs.getString("course_code"),
                        rs.getString("title"),
                        rs.getInt("credit")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching courses");
        }

        return list;
    }


    public boolean updateCourse(CourseUnit course) {
        String sql = "UPDATE course_unit SET title = ?, credit = ? WHERE course_code = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, course.getTitle());
            ps.setInt(2, course.getCredit());
            ps.setString(3, course.getCourseCode());

            int rows = ps.executeUpdate();
            return rows > 0; // 🔧 better than throwing error

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating course");
        }
    }


    public boolean deleteCourse(String code) {
        boolean previousAutoCommit = true;
        try {
            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            deleteByCourse("DELETE FROM medical_attendance WHERE attendance_id IN (\n"
                    + "    SELECT attendance_id FROM attendance WHERE course_code = ?\n"
                    + ")", code, false);
            deleteByCourse("DELETE FROM exam_medical WHERE course_code = ?", code, false);
            deleteByCourse("DELETE FROM marks WHERE course_code = ?", code, false);
            deleteByCourse("DELETE FROM attendance WHERE course_code = ?", code, false);
            deleteByCourse("DELETE FROM course_exam WHERE course_code = ?", code, false);
            deleteByCourse("DELETE FROM enrollment WHERE course_code = ?", code, false);
            deleteByCourse("DELETE FROM course_material WHERE course_code = ?", code, true);
            deleteByCourse("DELETE FROM lecturer_course WHERE course_code = ?", code, false);
            deleteByCourse("DELETE FROM timetable WHERE course_code = ?", code, false);

            int rows;
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM course_unit WHERE course_code = ?")) {
                ps.setString(1, code);
                rows = ps.executeUpdate();
            }
            connection.commit();
            return rows > 0;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Error deleting course: " + e.getMessage(), e);
        } finally {
            try { connection.setAutoCommit(previousAutoCommit); } catch (SQLException ignored) {}
        }
    }

    private void deleteByCourse(String sql, String code, boolean ignoreMissingTable) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            if (ignoreMissingTable && "42S02".equals(e.getSQLState())) {
                return;
            }
            throw e;
        }
    }


    public List<CourseUnit> searchCourses(String keyword) {
        List<CourseUnit> list = new ArrayList<>();
        String sql = "SELECT * FROM course_unit WHERE course_code LIKE ? OR title LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CourseUnit(
                            rs.getString("course_code"),
                            rs.getString("title"),
                            rs.getInt("credit")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error searching courses");
        }

        return list;
    }


    public boolean courseExists(String code) {
        String sql = "SELECT 1 FROM course_unit WHERE course_code = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking course");
        }
    }
}
