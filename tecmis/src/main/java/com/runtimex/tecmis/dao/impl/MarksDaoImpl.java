package com.runtimex.tecmis.dao.impl;

import com.runtimex.tecmis.dao.MarksDao;
import com.runtimex.tecmis.models.Mark;
import com.runtimex.tecmis.models.CourseExam;
import com.runtimex.tecmis.models.CourseUnit;
import com.runtimex.tecmis.models.MarkEntry;
import com.runtimex.tecmis.models.StudentInfo;

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

    @Override
    public List<CourseExam> getCourseExams(String courseCode) {
        List<CourseExam> exams = new ArrayList<>();

        String sql = """
                SELECT ce.course_code, ce.type_id, ce.weight, ce.exam_date, ce.exam_name, et.type_name
                FROM course_exam ce
                JOIN exam_type et ON et.type_id = ce.type_id
                WHERE ce.course_code = ?
                ORDER BY ce.exam_date
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CourseExam exam = new CourseExam();
                exam.setCourseCode(rs.getString("course_code"));
                exam.setExamTypeId(rs.getString("type_id"));
                exam.setWeight(rs.getDouble("weight"));
                exam.setExamDate(rs.getString("exam_date"));
                exam.setExamName(rs.getString("exam_name"));
                exam.setExamTypeName(rs.getString("type_name"));
                exams.add(exam);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching course exams", e);
        }

        return exams;
    }

    @Override
    public List<CourseUnit> getAllCourses() {
        List<CourseUnit> courses = new ArrayList<>();

        String sql = """
                SELECT course_code, title, credit
                FROM course_unit
                ORDER BY course_code
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CourseUnit unit = new CourseUnit();
                unit.setCourseCode(rs.getString("course_code"));
                unit.setTitle(rs.getString("title"));
                unit.setCredit(rs.getInt("credit"));
                courses.add(unit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching courses", e);
        }

        return courses;
    }

    @Override
    public List<CourseUnit> getCoursesByStudent(String stuId) {
        List<CourseUnit> courses = new ArrayList<>();

        String sql = """
                SELECT c.course_code, c.title, c.credit
                FROM enrollment e
                JOIN course_unit c ON c.course_code = e.course_code
                WHERE e.stu_id = ?
                ORDER BY c.course_code
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CourseUnit unit = new CourseUnit();
                unit.setCourseCode(rs.getString("course_code"));
                unit.setTitle(rs.getString("title"));
                unit.setCredit(rs.getInt("credit"));
                courses.add(unit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching student courses", e);
        }

        return courses;
    }

    @Override
    public List<StudentInfo> getStudentsByCourse(String courseCode) {
        List<StudentInfo> students = new ArrayList<>();

        String sql = """
                SELECT u.id AS stu_id,
                       CONCAT(u.f_name, ' ', u.l_name) AS student_name,
                       ug.status
                FROM enrollment e
                JOIN users u ON u.id = e.stu_id
                LEFT JOIN undergraduate ug ON ug.stu_id = e.stu_id
                WHERE e.course_code = ?
                ORDER BY u.id
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                students.add(new StudentInfo(
                        rs.getString("stu_id"),
                        rs.getString("student_name"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching students", e);
        }

        return students;
    }

    @Override
    public List<MarkEntry> getMarkEntries(String stuId, String courseCode) {
        List<MarkEntry> entries = new ArrayList<>();

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
                Double mark = rs.getObject("mark") == null ? null : rs.getDouble("mark");
                entries.add(new MarkEntry(
                        rs.getString("mark_id"),
                        rs.getString("stu_id"),
                        rs.getString("course_code"),
                        rs.getString("type_id"),
                        mark));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching mark entries", e);
        }

        return entries;
    }

    @Override
    public MarkEntry getMarkEntry(String stuId, String courseCode, String examTypeId) {
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
                Double mark = rs.getObject("mark") == null ? null : rs.getDouble("mark");
                return new MarkEntry(
                        rs.getString("mark_id"),
                        rs.getString("stu_id"),
                        rs.getString("course_code"),
                        rs.getString("type_id"),
                        mark);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching mark entry", e);
        }

        return null;
    }

    @Override
    public String getUndergraduateStatus(String stuId) {
        String sql = "SELECT status FROM undergraduate WHERE stu_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching undergraduate status", e);
        }

        return null;
    }

    @Override
    public boolean hasApprovedExamMedical(String stuId, String courseCode, String examTypeId) {
        String sql = """
                SELECT 1
                FROM exam_medical
                WHERE stu_id = ? AND course_code = ? AND type_id = ? AND status = 'Approved'
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ps.setString(2, courseCode);
            ps.setString(3, examTypeId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching exam medical status", e);
        }
    }

    @Override
    public boolean hasExamMedicalForCourse(String stuId, String courseCode) {
        String sql = """
                SELECT 1
                FROM exam_medical
                WHERE stu_id = ? AND course_code = ? AND status = 'Approved'
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ps.setString(2, courseCode);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching exam medical status", e);
        }
    }
}