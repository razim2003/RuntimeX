package com.runtimex.tecmis;

import com.runtimex.tecmis.dao.BlogDao;
import com.runtimex.tecmis.models.Blog;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlogDaoTest {

    private static Connection conn;
    private static BlogDao dao;

    private static final String TEST_USER = "USR_TEST_001";
    private static final String TEST_BLOG_ID = "BLOG_TEST_001";

    @BeforeAll
    static void setup() throws Exception {
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tecmis_java",
                "root",
                "1234"
        );

        dao = new BlogDao(conn);

        insertTestUser();
    }

    static void insertTestUser() throws Exception {
        conn.createStatement().execute("""
            INSERT IGNORE INTO users (id, f_name, l_name, email, contact_no, hash_pwd, user_type)
            VALUES ('USR_TEST_001', 'Blog', 'User', 'blog@test.com', '0700000000', 'hash', 'Undergraduate')
        """);
    }

    @AfterEach
    void cleanup() throws Exception {
        conn.createStatement().execute("DELETE FROM blog WHERE blog_id LIKE 'BLOG_TEST_%'");
    }

    @AfterAll
    static void close() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }

    // Helper
    private Blog buildBlog(String id, String title) {
        Blog b = new Blog();
        b.setBlogId(id);
        b.setUserId(TEST_USER);
        b.setTitle(title);
        b.setDate(new Date(System.currentTimeMillis()));
        return b;
    }

    // -------------------- CREATE --------------------

    @Test
    @Order(1)
    void testCreateBlog_Success() {
        Blog blog = buildBlog(TEST_BLOG_ID, "My First Blog");

        assertDoesNotThrow(() -> dao.createBlog(blog));
    }

    // -------------------- VIEW --------------------

    @Test
    @Order(2)
    void testViewMyBlogs_ReturnsInsertedBlog() {
        dao.createBlog(buildBlog(TEST_BLOG_ID, "Test Blog"));

        List<Blog> list = dao.viewMyBlogs(TEST_USER);

        assertFalse(list.isEmpty());
        assertTrue(list.stream().anyMatch(b -> b.getBlogId().equals(TEST_BLOG_ID)));
    }

    @Test
    @Order(3)
    void testViewMyBlogs_InvalidUser_ReturnsEmpty() {
        List<Blog> list = dao.viewMyBlogs("INVALID_USER");

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    // -------------------- UPDATE --------------------

    @Test
    @Order(4)
    void testUpdateBlog_Success() {
        dao.createBlog(buildBlog(TEST_BLOG_ID, "Old Title"));

        Blog updated = buildBlog(TEST_BLOG_ID, "New Title");
        dao.updateBlog(updated);

        List<Blog> list = dao.viewMyBlogs(TEST_USER);

        Blog result = list.stream()
                .filter(b -> b.getBlogId().equals(TEST_BLOG_ID))
                .findFirst()
                .orElse(null);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
    }

    @Test
    @Order(5)
    void testUpdateBlog_NotFound() {
        Blog blog = buildBlog("INVALID_ID", "Nothing");

        assertDoesNotThrow(() -> dao.updateBlog(blog));
    }

    // -------------------- DELETE --------------------

    @Test
    @Order(6)
    void testDeleteBlog_Success() {
        dao.createBlog(buildBlog(TEST_BLOG_ID, "To Delete"));

        dao.deleteBlog(TEST_BLOG_ID, TEST_USER);

        List<Blog> list = dao.viewMyBlogs(TEST_USER);

        assertFalse(list.stream().anyMatch(b -> b.getBlogId().equals(TEST_BLOG_ID)));
    }

    @Test
    @Order(7)
    void testDeleteBlog_NotFound() {
        assertDoesNotThrow(() -> dao.deleteBlog("INVALID", TEST_USER));
    }
}
