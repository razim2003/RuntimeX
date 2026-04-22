package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    private final Connection connection;

    public EventDAO(Connection connection) {
        this.connection = connection;
    }


    public void addEvent(Event event) {
        String sql = "INSERT INTO event_cal (event_id, user_id, title, description, date, time) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, event.getEventId());
            ps.setString(2, event.getUserId());
            ps.setString(3, event.getTitle());
            ps.setString(4, event.getDescription());
            ps.setDate(5, Date.valueOf(event.getDate()));
            ps.setTime(6, Time.valueOf(event.getTime()));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding event");
        }
    }


    public Event getEventById(String id) {
        String sql = "SELECT * FROM event_cal WHERE event_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                            rs.getString("event_id"),
                            rs.getString("user_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDate("date").toLocalDate(),
                            rs.getTime("time").toLocalTime()
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching event");
        }

        return null;
    }


    public List<Event> getAllEvents() {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM event_cal";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Event(
                        rs.getString("event_id"),
                        rs.getString("user_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("time").toLocalTime()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching events");
        }

        return list;
    }


    public boolean updateEvent(Event event) {
        String sql = "UPDATE event_cal SET title = ?, description = ?, date = ?, time = ? WHERE event_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setDate(3, Date.valueOf(event.getDate()));
            ps.setTime(4, Time.valueOf(event.getTime()));
            ps.setString(5, event.getEventId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating event");
        }
    }


    public boolean deleteEvent(String id) {
        String sql = "DELETE FROM event_cal WHERE event_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, id);
            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting event");
        }
    }


    public List<Event> searchEvents(String keyword) {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM event_cal WHERE title LIKE ? OR description LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Event(
                            rs.getString("event_id"),
                            rs.getString("user_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDate("date").toLocalDate(),
                            rs.getTime("time").toLocalTime()
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error searching events");
        }

        return list;
    }


    public boolean eventExists(String id) {
        String sql = "SELECT 1 FROM event_cal WHERE event_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking event");
        }
    }
}