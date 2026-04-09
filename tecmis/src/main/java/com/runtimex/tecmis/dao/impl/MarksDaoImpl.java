package com.runtimex.tecmis.dao.impl;

import com.runtimex.tecmis.dao.MarksDao;
import com.runtimex.tecmis.models.Mark;
import com.runtimex.tecmis.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarksDaoImpl implements MarksDao {

    private final Connection connection;

    public MarksDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Mark> getMarksByStudentAndCourse(String stuId, String courseCode) {
        List<Mark> marks = new ArrayList<>();
        String sql = "SELECT * FROM marks WHERE stu_id = ? AND course_code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ps.setString(2, courseCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Mark mark = new Mark(
                        rs.getString("mark_id"),
                        rs.getString("stu_id"),
                        rs.getString("course_code"),
                        rs.getString("type_id"),
                        rs.getDouble("mark")
                );
                marks.add(mark);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marks;
    }

    @Override
    public List<Mark> getQuizMarksByStudentAndCourse(String stuId, String courseCode) {
        List<Mark> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM marks WHERE stu_id = ? AND course_code = ? AND type_id LIKE 'QU%'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ps.setString(2, courseCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Mark mark = new Mark(
                        rs.getString("mark_id"),
                        rs.getString("stu_id"),
                        rs.getString("course_code"),
                        rs.getString("type_id"),
                        rs.getDouble("mark")
                );
                quizzes.add(mark);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    @Override
    public void addMark(Mark mark) {
        String sql = "INSERT INTO marks (mark_id, stu_id, course_code, type_id, mark) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, mark.getMarkId());
            ps.setString(2, mark.getStuId());
            ps.setString(3, mark.getCourseCode());
            ps.setString(4, mark.getTypeId());
            ps.setDouble(5, mark.getMark());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMark(Mark mark) {
        String sql = "UPDATE marks SET mark = ? WHERE mark_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, mark.getMark());
            ps.setString(2, mark.getMarkId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMark(String markId) {
        String sql = "DELETE FROM marks WHERE mark_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, markId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasMedicalForExam(String stuId, String courseCode, String examTypeId) {
        String sql = "SELECT COUNT(*) FROM exam_medical WHERE stu_id = ? AND course_code = ? AND type_id = ? AND status = 'Approved'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ps.setString(2, courseCode);
            ps.setString(3, examTypeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}