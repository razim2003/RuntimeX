package main.java.com.runtimex.tecmis.dao;

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
        String sql = "DELETE FROM course_unit WHERE course_code = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting course");
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
