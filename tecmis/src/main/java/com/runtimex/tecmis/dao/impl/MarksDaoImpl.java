package com.runtimex.tecmis.dao.impl;

import com.runtimex.tecmis.dao.MarksDao;
import com.runtimex.tecmis.models.Mark;
import com.runtimex.tecmis.models.CourseExam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarksDaoImpl implements MarksDao {

    private final Connection connection;

    public MarksDaoImpl(Connection connection) {
        this.connection = connection;
    }

    // -----------------------------------------
    // GET ALL MARKS BY STUDENT + COURSE
    // -----------------------------------------
    @Override
    public List<Mark> getMarksByStudentAndCourse(String stuId, String courseCode) {

        List<Mark> marks = new ArrayList<>();

        String sql = """
                SELECT mark_id, stu_id, course_code, type_id, mark
                FROM marks
                WHERE stu_id = ? AND course_code = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, stuId);
            ps.setString(2, courseCode);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CourseExam courseExam = new CourseExam();
                courseExam.setCourseCode(rs.getString("course_code"));
                courseExam.setExamTypeId(rs.getString("type_id"));

                Mark mark = new Mark(
                        rs.getString("mark_id"),
                        rs.getString("stu_id"),
                        courseExam,
                        rs.getDouble("mark")
                );

                marks.add(mark);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching marks", e);
        }

        return marks;
    }

    // -----------------------------------------
    // GET SINGLE MARK
    // -----------------------------------------
    @Override
    public Mark getMark(String stuId, String courseCode, String examTypeId) {

        String sql = """
                SELECT mark_id, stu_id, course_code, type_id, mark
                FROM marks
                WHERE stu_id = ? AND course_code = ? AND type_id = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, stuId);
            ps.setString(2, courseCode);
            ps.setString(3, examTypeId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                CourseExam courseExam = new CourseExam();
                courseExam.setCourseCode(rs.getString("course_code"));
                courseExam.setExamTypeId(rs.getString("type_id"));

                return new Mark(
                        rs.getString("mark_id"),
                        rs.getString("stu_id"),
                        courseExam,
                        rs.getDouble("mark")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching mark", e);
        }

        return null;
    }


    @Override
    public void addMark(Mark mark) {

        String sql = """
                INSERT INTO marks (mark_id, stu_id, course_code, type_id, mark)
                VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, mark.getMarkId());
            ps.setString(2, mark.getStudentId());
            ps.setString(3, mark.getCourseExam().getCourseCode());
            ps.setString(4, mark.getCourseExam().getExamTypeId());
            ps.setDouble(5, mark.getMark());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting mark", e);
        }
    }


    @Override
    public void updateMark(Mark mark) {

        String sql = """
            UPDATE marks
            SET mark = ?
            WHERE mark_id = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, mark.getMark());
            ps.setString(2, mark.getMarkId());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("No mark found with ID: " + mark.getMarkId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating mark", e);
        }
    }    // -----------------------------------------
    // DELETE MARK
    // -----------------------------------------
    @Override
    public void deleteMark(String markId) {

        String sql = "DELETE FROM marks WHERE mark_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, markId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting mark", e);
        }
    }
}