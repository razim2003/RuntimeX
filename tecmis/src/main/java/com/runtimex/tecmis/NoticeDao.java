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

   
    public void createNotice(Notice n, String adminId) {

        String sql = "INSERT INTO notice (notice_id, admin_id, title, date) VALUES (?,?,?,?)";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, n.getNoticeId());
            pst.setString(2, adminId);
            pst.setString(3, n.getTitle());
            pst.setDate(4, new Date(System.currentTimeMillis()));

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error creating notice", e);
        }
    }

    
    public List<Notice> getAllNotices() {

        List<Notice> list = new ArrayList<>();
        String sql = "SELECT * FROM notice ORDER BY date DESC";

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Notice n = new Notice();

                n.setNoticeId(rs.getString("notice_id"));
                n.setAdminId(rs.getString("admin_id"));
                n.setTitle(rs.getString("title"));
                n.setDate(rs.getDate("date"));

                list.add(n);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading notices", e);
        }

        return list;
    }

    
    public List<Notice> searchNotices(String keyword) {

        List<Notice> list = new ArrayList<>();
        String sql = "SELECT * FROM notice WHERE title LIKE ? ORDER BY date DESC";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    Notice n = new Notice();

                    n.setNoticeId(rs.getString("notice_id"));
                    n.setAdminId(rs.getString("admin_id"));
                    n.setTitle(rs.getString("title"));
                    n.setDate(rs.getDate("date"));

                    list.add(n);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error searching notices", e);
        }

        return list;
    }

    
    public void updateNotice(Notice n, String adminId) {

        String sql = "UPDATE notice SET title=?, date=? WHERE notice_id=? AND admin_id=?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, n.getTitle());
            pst.setDate(2, new Date(System.currentTimeMillis()));
            pst.setString(3, n.getNoticeId());
            pst.setString(4, adminId);

            int rows = pst.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("Notice not found or no permission");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating notice", e);
        }
    }

    
    public void deleteNotice(String noticeId, String adminId) {

        String sql = "DELETE FROM notice WHERE notice_id=? AND admin_id=?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, noticeId);
            pst.setString(2, adminId);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting notice", e);
        }
    }
}
