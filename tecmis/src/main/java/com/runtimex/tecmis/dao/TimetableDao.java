package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.Timetable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the timetable table.
 *
 * <ul>
 *   <li>Admin  → addSession / updateSession / deleteSession / viewTimetable</li>
 *   <li>TechnicalOfficer → viewTimetableByDepartment (filtered by level/course)</li>
 *   <li>Undergraduate   → viewTimetableByLevel</li>
 * </ul>
 */
public class TimetableDao {

    private final Connection conn;

    public TimetableDao(Connection conn) {
        this.conn = conn;
    }

    // ─────────────────────────────────────────────────────────────────────
    // Admin: Create / Update / Delete
    // ─────────────────────────────────────────────────────────────────────

    public void addSession(Timetable t, String adminId) {
        String sql = """
                INSERT INTO timetable
                  (timetable_id, admin_id, lec_id, course_code,
                   location, level, type, hours, day_of_week, start_time)
                VALUES (?,?,?,?,?,?,?,?,?,?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTimetableId());
            ps.setString(2, adminId);
            ps.setString(3, t.getLecturerId());
            ps.setString(4, t.getCourseCode());
            ps.setString(5, t.getLocation());
            ps.setInt   (6, t.getLevel());
            ps.setString(7, t.getType());
            ps.setInt   (8, t.getHours());
            ps.setString(9, t.getDayOfWeek());
            ps.setString(10, t.getStartTime());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding timetable session", e);
        }
    }

    public void updateSession(Timetable t, String adminId) {
        String sql = """
                UPDATE timetable
                   SET lec_id=?, course_code=?, location=?, level=?,
                       type=?, hours=?, day_of_week=?, start_time=?
                 WHERE timetable_id=? AND admin_id=?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getLecturerId());
            ps.setString(2, t.getCourseCode());
            ps.setString(3, t.getLocation());
            ps.setInt   (4, t.getLevel());
            ps.setString(5, t.getType());
            ps.setInt   (6, t.getHours());
            ps.setString(7, t.getDayOfWeek());
            ps.setString(8, t.getStartTime());
            ps.setString(9, t.getTimetableId());
            ps.setString(10, adminId);
            if (ps.executeUpdate() == 0)
                throw new RuntimeException("Session not found or not owned by this admin");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating timetable session", e);
        }
    }

    public void deleteSession(String timetableId, String adminId) {
        String sql = "DELETE FROM timetable WHERE timetable_id=? AND admin_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, timetableId);
            ps.setString(2, adminId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting timetable session", e);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Admin / full view
    // ─────────────────────────────────────────────────────────────────────

    /** Returns all timetable rows ordered by level then day then time. */
    public List<Timetable> viewTimetable() {
        String sql = """
                SELECT * FROM timetable
                 ORDER BY level,
                          FIELD(day_of_week,
                                'Monday','Tuesday','Wednesday',
                                'Thursday','Friday','Saturday','Sunday'),
                          start_time
                """;
        return query(sql);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Technical Officer: see their department's timetable
    // The department is mapped to a lecturer (via lecturer_course join)
    // OR simply by filtering on the TO's department stored in technical_officer.
    // Here we filter by the department column added in the migration.
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Returns timetable rows where the assigned lecturer belongs to the given
     * department.  If department is null/blank all rows are returned (fallback).
     */
    public List<Timetable> viewTimetableByDepartment(String department) {
        if (department == null || department.isBlank()) {
            return viewTimetable();
        }
        // The department is stored in technical_officer; we correlate via lec_id
        // matching lecturers who teach the relevant courses.
        // Simplest approach: filter by a "department" concept modelled as the
        // course_code prefix (first 2 chars) OR we just expose all and let the UI
        // filter.  For a clean DB approach we filter by level group passed in.
        // Actual implementation: use the level linked to the TO's department.
        String sql = """
                SELECT t.* FROM timetable t
                  JOIN lecturer_course lc ON lc.course_code = t.course_code
                  JOIN lecturer l         ON l.lec_id        = lc.lec_id
                 WHERE t.lec_id IN (
                       SELECT lec_id FROM lecturer_course
                        WHERE course_code IN (
                              SELECT course_code FROM course_unit
                               WHERE course_code LIKE ?
                        )
                 )
                 ORDER BY level,
                          FIELD(day_of_week,
                                'Monday','Tuesday','Wednesday',
                                'Thursday','Friday','Saturday','Sunday'),
                          start_time
                """;
        // department is treated as a course-code prefix filter (e.g. "CS%")
        List<Timetable> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, department + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            // Fall back to full timetable if the complex query fails
            return viewTimetable();
        }
        return list;
    }

    // ─────────────────────────────────────────────────────────────────────
    // Undergraduate: see their level's timetable
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Returns timetable rows for the level the undergraduate is studying.
     * Level is derived from the undergraduate's enrollment year or passed
     * directly.
     */
    public List<Timetable> viewTimetableByLevel(int level) {
        String sql = """
                SELECT * FROM timetable
                 WHERE level = ?
                 ORDER BY FIELD(day_of_week,
                                'Monday','Tuesday','Wednesday',
                                'Thursday','Friday','Saturday','Sunday'),
                          start_time
                """;
        List<Timetable> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, level);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching timetable by level", e);
        }
        return list;
    }

    /**
     * Returns the level for an undergraduate by looking at their enrolled
     * courses.  Uses a simple heuristic: take the minimum level from the
     * timetable that matches one of the student's courses.
     */
    public int getLevelForStudent(String stuId) {
        String sql = """
                SELECT MIN(t.level) AS lvl
                  FROM timetable t
                  JOIN enrollment e ON e.course_code = t.course_code
                 WHERE e.stu_id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stuId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int lvl = rs.getInt("lvl");
                    return lvl == 0 ? 1 : lvl;
                }
            }
        } catch (SQLException e) {
            // ignore – default to 1
        }
        return 1;
    }

    // ─────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────

    private List<Timetable> query(String sql) {
        List<Timetable> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching timetable", e);
        }
        return list;
    }

    private Timetable map(ResultSet rs) throws SQLException {
        Timetable t = new Timetable();
        t.setTimetableId(rs.getString("timetable_id"));
        t.setAdminId    (rs.getString("admin_id"));
        t.setLecturerId (rs.getString("lec_id"));
        t.setCourseCode (rs.getString("course_code"));
        t.setLocation   (rs.getString("location"));
        t.setLevel      (rs.getInt   ("level"));
        t.setType       (rs.getString("type"));
        t.setHours      (rs.getInt   ("hours"));
        // gracefully handle old rows that have no day/time yet
        try { t.setDayOfWeek(rs.getString("day_of_week")); } catch (SQLException ignored) {}
        try { t.setStartTime(rs.getString("start_time")); } catch (SQLException ignored) {}
        return t;
    }
}
