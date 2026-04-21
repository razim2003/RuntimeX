package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.Blog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlogDao {

    private final Connection conn;

    public BlogDao(Connection conn) {
        this.conn = conn;
    }

    
	
    public void createBlog(Blog blog) {

        String sql = "INSERT INTO blog (blog_id, user_id, title, date) VALUES (?,?,?,?)";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, blog.getBlogId());
            pst.setString(2, blog.getUserId());
            pst.setString(3, blog.getTitle());
            pst.setDate(4, new java.sql.Date(blog.getDate().getTime()));

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error creating blog: " + blog.getBlogId(), e);
        }
    }

    
	
    public void updateBlog(Blog blog) {

        String sql = "UPDATE blog SET title=?, date=? WHERE blog_id=? AND user_id=?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, blog.getTitle());
            pst.setDate(2, new java.sql.Date(blog.getDate().getTime()));
            pst.setString(3, blog.getBlogId());
            pst.setString(4, blog.getUserId());

            int rows = pst.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("Blog not found or not owned by user");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating blog: " + blog.getBlogId(), e);
        }
    }

    
	
    public void deleteBlog(String blogId, String userId) {

        String sql = "DELETE FROM blog WHERE blog_id=? AND user_id=?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, blogId);
            pst.setString(2, userId);

            pst.executeUpdate(); // silent if not found

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting blog: " + blogId, e);
        }
    }

    
	
    public List<Blog> viewMyBlogs(String userId) {

        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM blog WHERE user_id=? ORDER BY date DESC";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, userId);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Blog blog = new Blog();

                blog.setBlogId(rs.getString("blog_id"));
                blog.setUserId(rs.getString("user_id"));
                blog.setTitle(rs.getString("title"));
                blog.setDate(rs.getDate("date"));

                list.add(blog);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving blogs for user: " + userId, e);
        }

        return list;
    }
}
