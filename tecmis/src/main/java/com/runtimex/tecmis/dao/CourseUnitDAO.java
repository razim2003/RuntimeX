package com.runtimex.tecmis.dao.impl;

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
            throw new RuntimeException("Error adding course", e);
        }
    }

    public List<CourseUnit> getAllCourses() {

        List<CourseUnit> list = new ArrayList<>();
        String sql = "SELECT * FROM course_unit";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                CourseUnit c = new CourseUnit(
                        rs.getString("course_code"),
                        rs.getString("title"),
                        rs.getInt("credit")
                );

                list.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching courses", e);
        }

        return list;
    }

    public CourseUnit getCourseById(String code) {

        String sql = "SELECT * FROM course_unit WHERE course_code = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new CourseUnit(
                        rs.getString("course_code"),
                        rs.getString("title"),
                        rs.getInt("credit")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching course", e);
        }
    }

    public void updateCourse(CourseUnit course) {

        String sql = "UPDATE course_unit SET title = ?, credit = ? WHERE course_code = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, course.getTitle());
            ps.setInt(2, course.getCredit());
            ps.setString(3, course.getCourseCode());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("Course not found for update");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating course", e);
        }
    }

    public void deleteCourse(String code) {

        String sql = "DELETE FROM course_unit WHERE course_code = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, code);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting course", e);
        }
    }

    public List<CourseUnit> searchCourses(String keyword) {

        List<CourseUnit> list = new ArrayList<>();
        String sql = "SELECT * FROM course_unit WHERE title LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CourseUnit c = new CourseUnit(
                        rs.getString("course_code"),
                        rs.getString("title"),
                        rs.getInt("credit")
                );

                list.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error searching courses", e);
        }

        return list;
    }

    public boolean courseExists(String courseCode) {

        String sql = "SELECT 1 FROM course_unit WHERE course_code = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error checking course existence", e);
        }
    }
}
