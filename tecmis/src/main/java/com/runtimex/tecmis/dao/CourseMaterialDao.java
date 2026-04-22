package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.CourseMaterial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for course_material table.
 *
 * <p>Lecturers can add / update / delete their own materials.
 * Undergraduates (and other roles) use {@link #getMaterialsByCourse(String)}
 * to read materials for a course they are enrolled in.</p>
 */
public class CourseMaterialDao {

    private final Connection conn;

    public CourseMaterialDao(Connection conn) {
        this.conn = conn;
    }

    // ─────────────────────────────────────────────────────────────────────
    // WRITE – Lecturer only
    // ─────────────────────────────────────────────────────────────────────

    /** Add a new material. lecId is taken from the authenticated lecturer. */
    public void addMaterial(CourseMaterial m) {
        String sql = """
                INSERT INTO course_material
                  (material_id, course_code, lec_id, title, description,
                   material_type, content, uploaded_date)
                VALUES (?,?,?,?,?,?,?,?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getMaterialId());
            ps.setString(2, m.getCourseCode());
            ps.setString(3, m.getLecId());
            ps.setString(4, m.getTitle());
            ps.setString(5, m.getDescription());
            ps.setString(6, m.getMaterialType());
            ps.setString(7, m.getContent());
            ps.setString(8, m.getUploadedDate());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding course material", e);
        }
    }

    /**
     * Update a material.
     * The WHERE clause checks lec_id so lecturers can only edit their own items.
     */
    public boolean updateMaterial(CourseMaterial m, String lecId) {
        String sql = """
                UPDATE course_material
                   SET title = ?, description = ?, material_type = ?,
                       content = ?, uploaded_date = ?
                 WHERE material_id = ? AND lec_id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getTitle());
            ps.setString(2, m.getDescription());
            ps.setString(3, m.getMaterialType());
            ps.setString(4, m.getContent());
            ps.setString(5, m.getUploadedDate());
            ps.setString(6, m.getMaterialId());
            ps.setString(7, lecId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating course material", e);
        }
    }

    /**
     * Delete a material.
     * Only the owning lecturer may delete.
     */
    public boolean deleteMaterial(String materialId, String lecId) {
        String sql = "DELETE FROM course_material WHERE material_id = ? AND lec_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, materialId);
            ps.setString(2, lecId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting course material", e);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────────────────────────────

    /** All materials for a given course (used by undergraduates and lecturers). */
    public List<CourseMaterial> getMaterialsByCourse(String courseCode) {
        String sql = """
                SELECT * FROM course_material
                 WHERE course_code = ?
                 ORDER BY uploaded_date DESC
                """;
        return query(sql, courseCode);
    }

    /** All materials uploaded by a specific lecturer (filtered by course too). */
    public List<CourseMaterial> getMaterialsByLecturerAndCourse(String lecId, String courseCode) {
        String sql = """
                SELECT * FROM course_material
                 WHERE lec_id = ? AND course_code = ?
                 ORDER BY uploaded_date DESC
                """;
        List<CourseMaterial> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lecId);
            ps.setString(2, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching materials", e);
        }
        return list;
    }

    /** Fetch a single material by ID. */
    public CourseMaterial getById(String materialId) {
        String sql = "SELECT * FROM course_material WHERE material_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching material", e);
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────

    private List<CourseMaterial> query(String sql, String param) {
        List<CourseMaterial> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying course materials", e);
        }
        return list;
    }

    private CourseMaterial map(ResultSet rs) throws SQLException {
        return new CourseMaterial(
                rs.getString("material_id"),
                rs.getString("course_code"),
                rs.getString("lec_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("material_type"),
                rs.getString("content"),
                rs.getString("uploaded_date") == null ? null
                        : rs.getString("uploaded_date")
        );
    }
}
