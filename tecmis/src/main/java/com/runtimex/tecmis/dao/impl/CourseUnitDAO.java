package com.runtimex.tecmis.dao.impl;

import com.runtimex.tecmis.models.CourseUnit;
import com.runtimex.tecmis.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseUnitDAO {

    // 🔹 Add Course
    public boolean addCourse(CourseUnit course) {

        String sql = "INSERT INTO course_unit (course_code, title, credit) VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, course.getCourseCode());
            ps.setString(2, course.getTitle());
            ps.setInt(3, course.getCredit());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Get All Courses
    public List<CourseUnit> getAllCourses() {

        List<CourseUnit> list = new ArrayList<>();
        String sql = "SELECT * FROM course_unit";

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                CourseUnit c = new CourseUnit();
                c.setCourseCode(rs.getString("course_code"));
                c.setTitle(rs.getString("title"));
                c.setCredit(rs.getInt("credit"));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 🔹 Get Course By ID
    public CourseUnit getCourseById(String code) {

        String sql = "SELECT * FROM course_unit WHERE course_code = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ps.setString(1, code);

            if (rs.next()) {

                CourseUnit c = new CourseUnit();
                c.setCourseCode(rs.getString("course_code"));
                c.setTitle(rs.getString("title"));
                c.setCredit(rs.getInt("credit"));

                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 🔹 Update Course
    public boolean updateCourse(CourseUnit course) {

        String sql = "UPDATE course_unit SET title = ?, credit = ? WHERE course_code = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, course.getTitle());
            ps.setInt(2, course.getCredit());
            ps.setString(3, course.getCourseCode());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Delete Course
    public boolean deleteCourse(String code) {

        String sql = "DELETE FROM course_unit WHERE course_code = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Search Courses
    public List<CourseUnit> searchCourses(String keyword) {

        List<CourseUnit> list = new ArrayList<>();
        String sql = "SELECT * FROM course_unit WHERE title LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CourseUnit c = new CourseUnit();
                c.setCourseCode(rs.getString("course_code"));
                c.setTitle(rs.getString("title"));
                c.setCredit(rs.getInt("credit"));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 🔹 Check if Course Exists (IMPORTANT)
    public boolean courseExists(String courseCode) {

        String sql = "SELECT 1 FROM course_unit WHERE course_code = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseCode);

            ResultSet rs = ps.executeQuery();

            return rs.next(); // true if exists

        } catch (Exception e) {
            System.out.println("Course check error: " + e.getMessage());
            return false;
        }
    }
}