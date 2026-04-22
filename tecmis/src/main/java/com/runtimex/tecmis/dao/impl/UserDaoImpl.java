package com.runtimex.tecmis.dao.impl;

import com.runtimex.tecmis.dao.UserDao;
import com.runtimex.tecmis.models.AuthUser;
import com.runtimex.tecmis.models.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private final Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUser authenticate(String userId, String password) {
        String sql = """
                SELECT id, f_name, l_name, email, user_type, hash_pwd
                FROM users
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            String stored = rs.getString("hash_pwd");
            // For demo seed data, allow direct hash match OR universal demo password.
            boolean ok = stored != null && stored.equals(password);
            if (!ok && "1234".equals(password)) {
                ok = true;
            }

            if (!ok) {
                return null;
            }

            return new AuthUser(
                    rs.getString("id"),
                    rs.getString("f_name") + " " + rs.getString("l_name"),
                    rs.getString("email"),
                    rs.getString("user_type"));
        } catch (SQLException e) {
            throw new RuntimeException("Error while authenticating user", e);
        }
    }

    @Override
    public UserProfile findById(String userId) {
        String sql = """
                SELECT u.id, u.f_name, u.l_name, u.email, u.contact_no, u.profile_image_path, u.user_type, ug.status
                    FROM users u
                    LEFT JOIN undergraduate ug ON ug.stu_id = u.id
                    WHERE u.id = ?
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            return new UserProfile(
                    rs.getString("id"),
                    rs.getString("f_name"),
                    rs.getString("l_name"),
                    rs.getString("email"),
                    rs.getString("contact_no"),
                    rs.getString("profile_image_path"),
                    rs.getString("user_type"),
                    rs.getString("status"));
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading user profile", e);
        }
    }

    @Override
    public List<UserProfile> findUsers(String userType, String keyword) {
        List<UserProfile> users = new ArrayList<>();

        String sql = """
                SELECT u.id, u.f_name, u.l_name, u.email, u.contact_no, u.profile_image_path, u.user_type, ug.status
                    FROM users u
                    LEFT JOIN undergraduate ug ON ug.stu_id = u.id
                    WHERE (? = 'All' OR u.user_type = ?)
                      AND (
                            ? = ''
                            OR u.id LIKE CONCAT('%', ?, '%')
                            OR u.f_name LIKE CONCAT('%', ?, '%')
                            OR u.l_name LIKE CONCAT('%', ?, '%')
                            OR u.email LIKE CONCAT('%', ?, '%')
                          )
                    ORDER BY u.user_type, u.id
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userType);
            ps.setString(2, userType);
            ps.setString(3, keyword);
            ps.setString(4, keyword);
            ps.setString(5, keyword);
            ps.setString(6, keyword);
            ps.setString(7, keyword);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new UserProfile(
                        rs.getString("id"),
                        rs.getString("f_name"),
                        rs.getString("l_name"),
                        rs.getString("email"),
                        rs.getString("contact_no"),
                        rs.getString("profile_image_path"),
                        rs.getString("user_type"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while searching users", e);
        }

        return users;
    }

    @Override
    public void updateUserContact(String userId, String email, String contactNo) {
        String sql = "UPDATE users SET email = ?, contact_no = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, contactNo);
            ps.setString(3, userId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("No user found for ID: " + userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating user profile", e);
        }
    }

    @Override
    public void updateMyProfile(String userId, String email, String contactNo, String profileImagePath) {
        String sql = "UPDATE users SET email = ?, contact_no = ?, profile_image_path = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, contactNo);
            ps.setString(3, profileImagePath == null || profileImagePath.isBlank() ? null : profileImagePath);
            ps.setString(4, userId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("No user found for ID: " + userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating user profile", e);
        }
    }

    @Override
    public void createUser(String id, String firstName, String lastName, String email,
                           String contactNo, String password, String userType) {
        String insertUser = "INSERT INTO users (id, f_name, l_name, email, contact_no, hash_pwd, user_type) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(insertUser)) {
            ps.setString(1, id);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, email);
            ps.setString(5, contactNo);
            ps.setString(6, password);
            ps.setString(7, userType);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }

        // Insert into role-specific table
        String roleTable;
        String roleCol;
        switch (userType) {
            case "Admin"            -> { roleTable = "admin";            roleCol = "admin_id"; }
            case "Lecturer"         -> { roleTable = "lecturer";         roleCol = "lec_id"; }
            case "TechnicalOfficer" -> { roleTable = "technical_officer"; roleCol = "to_id"; }
            case "Undergraduate"    -> { roleTable = "undergraduate";    roleCol = "stu_id"; }
            default -> throw new RuntimeException("Unknown user type: " + userType);
        }

        String insertRole;
        if ("Lecturer".equals(userType)) {
            insertRole = "INSERT INTO lecturer (lec_id, designation) VALUES (?, 'Lecturer')";
        } else if ("Undergraduate".equals(userType)) {
            insertRole = "INSERT INTO undergraduate (stu_id, status) VALUES (?, 'Proper')";
        } else {
            insertRole = "INSERT INTO " + roleTable + " (" + roleCol + ") VALUES (?)";
        }

        try (PreparedStatement ps = connection.prepareStatement(insertRole)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Rollback user insert
            try (PreparedStatement del = connection.prepareStatement("DELETE FROM users WHERE id=?")) {
                del.setString(1, id); del.executeUpdate();
            } catch (SQLException ignored) {}
            throw new RuntimeException("Error creating role record: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            int rows = ps.executeUpdate();
            if (rows == 0) throw new RuntimeException("No user found for ID: " + userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    @Override
    public List<UserProfile> findUndergraduates(String keyword) {
        List<UserProfile> users = new ArrayList<>();
        String sql = """
                SELECT u.id, u.f_name, u.l_name, u.email, u.contact_no, u.profile_image_path, u.user_type, ug.status
                    FROM users u
                    JOIN undergraduate ug ON ug.stu_id = u.id
                    WHERE (
                            ? = ''
                            OR u.id LIKE CONCAT('%', ?, '%')
                            OR u.f_name LIKE CONCAT('%', ?, '%')
                            OR u.l_name LIKE CONCAT('%', ?, '%')
                          )
                    ORDER BY u.id
                    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new UserProfile(
                        rs.getString("id"),
                        rs.getString("f_name"),
                        rs.getString("l_name"),
                        rs.getString("email"),
                        rs.getString("contact_no"),
                        rs.getString("profile_image_path"),
                        rs.getString("user_type"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while searching undergraduates", e);
        }
        return users;
    }
}
