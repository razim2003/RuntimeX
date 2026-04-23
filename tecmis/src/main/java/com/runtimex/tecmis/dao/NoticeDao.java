package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.Notice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeDao {

    private final Connection conn;

    public NoticeDao(Connection conn) {
        this.conn = conn;
    }

    // ── helper: map a ResultSet row → Notice ──────────────────────────────
    private Notice map(ResultSet rs) throws SQLException {
        Notice n = new Notice();
        n.setNoticeId(rs.getString("notice_id"));
        n.setAdminId (rs.getString("admin_id"));
        n.setTitle   (rs.getString("title"));
        n.setDate    (rs.getDate  ("date"));
        n.setFilePath(rs.getString("file_path"));
        n.setFileType(rs.getString("file_type"));
        n.setAudience(rs.getString("audience"));
        return n;
    }

    // ── CREATE ────────────────────────────────────────────────────────────
    public void createNotice(Notice n, String adminId) {
        String sql = """
                INSERT INTO notice (notice_id, admin_id, title, date, file_path, file_type, audience)
                VALUES (?,?,?,?,?,?,?)
                """;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, n.getNoticeId());
            pst.setString(2, adminId);
            pst.setString(3, n.getTitle());
            pst.setDate  (4, new Date(System.currentTimeMillis()));
            pst.setString(5, blankToNull(n.getFilePath()));
            pst.setString(6, blankToNull(n.getFileType()));
            pst.setString(7, blankToNull(n.getAudience()) == null ? "All" : n.getAudience());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating notice: " + e.getMessage(), e);
        }
    }

    // ── READ ALL ──────────────────────────────────────────────────────────
    public List<Notice> getAllNotices() {
        List<Notice> list = new ArrayList<>();
        String sql = "SELECT * FROM notice ORDER BY date DESC, notice_id DESC";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading notices: " + e.getMessage(), e);
        }
        return list;
    }

    // ── SEARCH ────────────────────────────────────────────────────────────
    public List<Notice> searchNotices(String keyword) {
        List<Notice> list = new ArrayList<>();
        String sql = "SELECT * FROM notice WHERE title LIKE ? ORDER BY date DESC, notice_id DESC";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching notices: " + e.getMessage(), e);
        }
        return list;
    }

    // ── READ BY AUDIENCE ────────────────────────────────────────────────
    public List<Notice> getNoticesForAudience(String audience) {
        List<Notice> list = new ArrayList<>();
        String sql = """
                SELECT * FROM notice
                WHERE audience = 'All' OR audience = ?
                ORDER BY date DESC, notice_id DESC
                """;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, audience);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading notices: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Notice> searchNoticesForAudience(String keyword, String audience) {
        List<Notice> list = new ArrayList<>();
        String sql = """
                SELECT * FROM notice
                WHERE (audience = 'All' OR audience = ?)
                  AND title LIKE ?
                ORDER BY date DESC, notice_id DESC
                """;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, audience);
            pst.setString(2, "%" + keyword + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching notices: " + e.getMessage(), e);
        }
        return list;
    }

    // ── UPDATE (title + optional new file) ───────────────────────────────
    public void updateNotice(Notice n, String adminId) {
        String sql = """
                UPDATE notice
                SET title=?, date=?, file_path=?, file_type=?, audience=?
                WHERE notice_id=? AND admin_id=?
                """;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, n.getTitle());
            pst.setDate  (2, new Date(System.currentTimeMillis()));
            pst.setString(3, blankToNull(n.getFilePath()));
            pst.setString(4, blankToNull(n.getFileType()));
            pst.setString(5, blankToNull(n.getAudience()) == null ? "All" : n.getAudience());
            pst.setString(6, n.getNoticeId());
            pst.setString(7, adminId);
            int rows = pst.executeUpdate();
            if (rows == 0) throw new RuntimeException("Notice not found or no permission to edit.");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating notice: " + e.getMessage(), e);
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────
    public void deleteNotice(String noticeId, String adminId) {
        String sql = "DELETE FROM notice WHERE notice_id=? AND admin_id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, noticeId);
            pst.setString(2, adminId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting notice: " + e.getMessage(), e);
        }
    }

    // ── utility ───────────────────────────────────────────────────────────
    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
