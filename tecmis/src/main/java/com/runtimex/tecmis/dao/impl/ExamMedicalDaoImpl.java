package com.runtimex.tecmis.dao.impl;

import com.runtimex.tecmis.dao.ExamMedicalDao;
import com.runtimex.tecmis.models.CourseExam;
import com.runtimex.tecmis.models.ExamMedical;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamMedicalDaoImpl implements ExamMedicalDao {

    private final Connection connection;

    public ExamMedicalDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(ExamMedical medical) {
        String sql = """
            INSERT INTO exam_medical (ex_med_ref_no, stu_id, course_code, type_id, status, submitted_date, proof_image_path)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, medical.getRefNo());
            ps.setString(2, medical.getStudentId());
            ps.setString(3, medical.getCourseExam().getCourseCode());
            ps.setString(4, medical.getCourseExam().getExamTypeId());
            ps.setString(5, medical.getStatus());
            ps.setString(6, medical.getSubmittedDate());
            ps.setString(7, medical.getProofImagePath());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating exam medical request", e);
        }
    }

    @Override
    public List<ExamMedical> getPending() {
        List<ExamMedical> list = new ArrayList<>();
        String sql = """
                SELECT ex_med_ref_no, stu_id, course_code, type_id, status, submitted_date, proof_image_path
                FROM exam_medical
                WHERE status = 'Pending'
                ORDER BY submitted_date DESC, ex_med_ref_no DESC
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CourseExam exam = new CourseExam();
                exam.setCourseCode(rs.getString("course_code"));
                exam.setExamTypeId(rs.getString("type_id"));
                ExamMedical med = new ExamMedical(
                        rs.getString("ex_med_ref_no"),
                        rs.getString("stu_id"),
                        exam,
                        rs.getString("status"),
                    rs.getString("submitted_date"),
                    rs.getString("proof_image_path"));
                list.add(med);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading pending exam medical requests", e);
        }
        return list;
    }

    @Override
    public List<ExamMedical> getByStudent(String stuId) {
        List<ExamMedical> list = new ArrayList<>();
        String sql = """
                SELECT ex_med_ref_no, stu_id, course_code, type_id, status, submitted_date, proof_image_path
                FROM exam_medical
                WHERE stu_id = ?
                ORDER BY submitted_date DESC, ex_med_ref_no DESC
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CourseExam exam = new CourseExam();
                    exam.setCourseCode(rs.getString("course_code"));
                    exam.setExamTypeId(rs.getString("type_id"));
                    ExamMedical med = new ExamMedical(
                            rs.getString("ex_med_ref_no"),
                            rs.getString("stu_id"),
                            exam,
                            rs.getString("status"),
                            rs.getString("submitted_date"),
                            rs.getString("proof_image_path"));
                    list.add(med);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading exam medical requests", e);
        }
        return list;
    }

    @Override
    public void updateStatus(String refNo, String status) {
        String sql = "UPDATE exam_medical SET status = ? WHERE ex_med_ref_no = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, refNo);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("No exam medical found for ref: " + refNo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating exam medical status", e);
        }
    }

    @Override
    public boolean exists(String stuId, String courseCode, String typeId) {
        String sql = """
                SELECT 1
                FROM exam_medical
                WHERE stu_id = ? AND course_code = ? AND type_id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ps.setString(2, courseCode);
            ps.setString(3, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking exam medical existence", e);
        }
    }

    @Override
    public ExamMedical getByRef(String refNo) {
        String sql = """
                SELECT ex_med_ref_no, stu_id, course_code, type_id, status, submitted_date, proof_image_path
                FROM exam_medical
                WHERE ex_med_ref_no = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, refNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                CourseExam exam = new CourseExam();
                exam.setCourseCode(rs.getString("course_code"));
                exam.setExamTypeId(rs.getString("type_id"));
                return new ExamMedical(
                        rs.getString("ex_med_ref_no"),
                        rs.getString("stu_id"),
                        exam,
                        rs.getString("status"),
                    rs.getString("submitted_date"),
                    rs.getString("proof_image_path"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching exam medical", e);
        }
    }

    @Override
    public boolean enrollmentExists(String stuId, String courseCode) {
        String sql = "SELECT 1 FROM enrollment WHERE stu_id = ? AND course_code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ps.setString(2, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking enrollment", e);
        }
    }

    @Override
    public boolean courseExamExists(String courseCode, String typeId) {
        String sql = "SELECT 1 FROM course_exam WHERE course_code = ? AND type_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseCode);
            ps.setString(2, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking course exam", e);
        }
    }

    @Override
    public boolean hasMark(String stuId, String courseCode, String typeId) {
        String sql = """
                SELECT 1
                FROM marks
                WHERE stu_id = ? AND course_code = ? AND type_id = ? AND mark IS NOT NULL
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ps.setString(2, courseCode);
            ps.setString(3, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking marks", e);
        }
    }
}
