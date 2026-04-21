package com.runtimex.tecmis.dao.impl;

import com.runtimex.tecmis.dao.MedicalDao;
import com.runtimex.tecmis.models.MedicalRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicalDaoImpl implements MedicalDao {

    private final Connection connection;

    public MedicalDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addMedical(MedicalRecord record) {
        String sql = """
                INSERT INTO medical (ref_no, stu_id, reason, status, start_date, end_date, proof_image_path)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, record.getRefNo());
            ps.setString(2, record.getStudentId());
            ps.setString(3, record.getReason());
            ps.setString(4, record.getStatus());
            ps.setString(5, record.getStartDate());
            ps.setString(6, record.getEndDate());
            ps.setString(7, record.getProofImagePath());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving medical record", e);
        }
    }

    @Override
    public void updateMedical(MedicalRecord record) {
        String sql = """
                UPDATE medical
                SET reason = ?, status = ?, start_date = ?, end_date = ?, proof_image_path = ?
                WHERE ref_no = ? AND stu_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, record.getReason());
            ps.setString(2, record.getStatus());
            ps.setString(3, record.getStartDate());
            ps.setString(4, record.getEndDate());
            ps.setString(5, record.getProofImagePath());
            ps.setString(6, record.getRefNo());
            ps.setString(7, record.getStudentId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("No medical record found for ref: " + record.getRefNo());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating medical record", e);
        }
    }

    @Override
    public void updateMedicalStatus(String refNo, String status) {
        String sql = "UPDATE medical SET status = ? WHERE ref_no = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, refNo);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("No medical record found for ref: " + refNo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating medical status", e);
        }
    }

    @Override
    public void linkMedicalToAttendance(String attendanceId, String refNo) {
        String sql = "INSERT INTO medical_attendance (attendance_id, ref_no) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, attendanceId);
            ps.setString(2, refNo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while linking medical to attendance", e);
        }
    }

    @Override
    public List<MedicalRecord> findMedicalByStudent(String studentId) {
        List<MedicalRecord> records = new ArrayList<>();

        String sql = """
                SELECT ref_no, stu_id, reason, status, start_date, end_date, proof_image_path
                    FROM medical
                    WHERE (? = '' OR stu_id = ?)
                    ORDER BY start_date DESC
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, studentId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                records.add(new MedicalRecord(
                        rs.getString("ref_no"),
                        rs.getString("stu_id"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("proof_image_path")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading medical records", e);
        }

        return records;
    }
}
