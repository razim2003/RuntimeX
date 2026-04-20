package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.Timetable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimetableDao {

    private final Connection conn;

    public TimetableDao(Connection conn) {
        this.conn = conn;
    }

    
	
    public void addSession(Timetable t, String adminId) {
        String sql = "INSERT INTO timetable (timetable_id, admin_id, lec_id, course_code, location, level, type, hours) VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, t.getTimetableId());
            pst.setString(2, adminId);
            pst.setString(3, t.getLecturerId());
            pst.setString(4, t.getCourseCode());
            pst.setString(5, t.getLocation());
            pst.setInt(6, t.getLevel());
            pst.setString(7, t.getType());
            pst.setInt(8, t.getHours());

            pst.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error adding session", e);
        }
    }

    
	
    public void updateSession(Timetable t, String adminId) {
        String sql = "UPDATE timetable SET lec_id=?, course_code=?, location=?, level=?, type=?, hours=? WHERE timetable_id=? AND admin_id=?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, t.getLecturerId());
            pst.setString(2, t.getCourseCode());
            pst.setString(3, t.getLocation());
            pst.setInt(4, t.getLevel());
            pst.setString(5, t.getType());
            pst.setInt(6, t.getHours());
            pst.setString(7, t.getTimetableId());
            pst.setString(8, adminId);

            int rows = pst.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("Session not found or not allowed");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error updating session", e);
        }
    }

    
	
	
    public void deleteSession(String timetableId, String adminId) {
        String sql = "DELETE FROM timetable WHERE timetable_id=? AND admin_id=?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, timetableId);
            pst.setString(2, adminId);

            pst.executeUpdate(); // no exception if not found

        } catch (Exception e) {
            throw new RuntimeException("Error deleting session", e);
        }
    }

    
	
	
    public List<Timetable> viewTimetable() {

        List<Timetable> list = new ArrayList<>();
        String sql = "SELECT * FROM timetable ORDER BY level";

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Timetable t = new Timetable();

                t.setTimetableId(rs.getString("timetable_id"));
                t.setAdminId(rs.getString("admin_id"));
                t.setLecturerId(rs.getString("lec_id"));
                t.setCourseCode(rs.getString("course_code"));
                t.setLocation(rs.getString("location"));
                t.setLevel(rs.getInt("level"));
                t.setType(rs.getString("type"));
                t.setHours(rs.getInt("hours"));

                list.add(t);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error viewing timetable", e);
        }

        return list;
    }
}
