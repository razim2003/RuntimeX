package com.runtimex.tecmis.dao.impl;

import com.runtimex.tecmis.dao.AttendanceDao;
import com.runtimex.tecmis.models.AttendanceRecord;
import com.runtimex.tecmis.models.AttendanceSummary;
import com.runtimex.tecmis.models.CourseUnit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDaoImpl implements AttendanceDao {

    private final Connection connection;

    public AttendanceDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addAttendance(AttendanceRecord record) {
        String sql = """
                INSERT INTO attendance (attendance_id, stu_id, course_code, session_date, component, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, record.getAttendanceId());
            ps.setString(2, record.getStudentId());
            ps.setString(3, record.getCourseCode());
            ps.setString(4, record.getSessionDate());
            ps.setString(5, record.getComponent());
            ps.setString(6, record.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding attendance", e);
        }
    }

    @Override
    public void updateAttendanceStatus(String attendanceId, String status) {
        String sql = "UPDATE attendance SET status = ? WHERE attendance_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, attendanceId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("No attendance found for id: " + attendanceId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating attendance", e);
        }
    }

    @Override
    public List<AttendanceRecord> findAttendance(String studentId, String courseCode, String component) {
        List<AttendanceRecord> records = new ArrayList<>();

        String sql = """
                SELECT a.attendance_id, a.stu_id, a.course_code, a.session_date, a.component, a.status,
                       ma.ref_no, m.status AS medical_status
                FROM attendance a
                LEFT JOIN medical_attendance ma ON ma.attendance_id = a.attendance_id
                LEFT JOIN medical m ON m.ref_no = ma.ref_no
                WHERE (? = '' OR a.stu_id = ?)
                  AND (? = '' OR a.course_code = ?)
                  AND (? = 'Combined' OR a.component = ?)
                ORDER BY a.session_date DESC, a.component
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, studentId);
            ps.setString(3, courseCode);
            ps.setString(4, courseCode);
            ps.setString(5, component);
            ps.setString(6, component);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AttendanceRecord record = new AttendanceRecord(
                        rs.getString("attendance_id"),
                        rs.getString("stu_id"),
                        rs.getString("course_code"),
                        rs.getString("session_date"),
                        rs.getString("component"),
                        rs.getString("status"));
                record.setMedicalRefNo(rs.getString("ref_no"));
                record.setMedicalStatus(rs.getString("medical_status"));
                records.add(record);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading attendance records", e);
        }

        return records;
    }

    @Override
    public List<AttendanceSummary> getAttendanceSummaryByCourse(String courseCode, String component,
            boolean includeApprovedMedical) {
        List<AttendanceSummary> summaries = new ArrayList<>();

        String sql = """
                SELECT u.id AS stu_id,
                       CONCAT(u.f_name, ' ', u.l_name) AS student_name,
                       COUNT(a.attendance_id) AS total_sessions,
                       COALESCE(SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END), 0) AS present_sessions,
                       COALESCE(SUM(CASE
                           WHEN a.status = 'Absent' AND m.status = 'Approved' THEN 1
                           ELSE 0
                       END), 0) AS approved_medical_absences
                FROM enrollment e
                JOIN undergraduate ug ON ug.stu_id = e.stu_id
                JOIN users u ON u.id = ug.stu_id
                LEFT JOIN attendance a ON a.stu_id = e.stu_id
                                       AND a.course_code = e.course_code
                                       AND (? = 'Combined' OR a.component = ?)
                LEFT JOIN medical_attendance ma ON ma.attendance_id = a.attendance_id
                LEFT JOIN medical m ON m.ref_no = ma.ref_no
                WHERE e.course_code = ?
                GROUP BY u.id, u.f_name, u.l_name
                ORDER BY u.id
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, component);
            ps.setString(2, component);
            ps.setString(3, courseCode);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AttendanceSummary summary = new AttendanceSummary(
                        rs.getString("stu_id"),
                        rs.getString("student_name"),
                        rs.getInt("total_sessions"),
                        rs.getInt("present_sessions"),
                        rs.getInt("approved_medical_absences"));

                int effectivePresent = summary.getPresentSessions();
                if (includeApprovedMedical) {
                    effectivePresent += summary.getApprovedMedicalAbsences();
                }

                summary.setEffectivePresent(effectivePresent);
                double percentage = summary.getTotalSessions() == 0
                        ? 0.0
                        : (effectivePresent * 100.0 / summary.getTotalSessions());
                summary.setPercentage(percentage);
                summary.setEligible(percentage >= 80.0);
                summaries.add(summary);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while building attendance summary", e);
        }

        return summaries;
    }

    @Override
    public double getAttendancePercentage(String stuId, String courseCode) {
        String sql = """
                SELECT COUNT(*) AS total,
                       SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) AS present
                FROM attendance
                WHERE stu_id = ? AND course_code = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stuId);
            ps.setString(2, courseCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                if (total == 0) {
                    return 0.0;
                }
                return present * 100.0 / total;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while calculating attendance percentage", e);
        }

        return 0.0;
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
                CourseUnit c = new CourseUnit();
                c.setCourseCode(rs.getString("course_code"));
                c.setTitle(rs.getString("title"));
                c.setCredit(rs.getInt("credit"));
                courses.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while loading student courses", e);
        }

        return courses;
    }
}
