package com.runtimex.tecmis;

import com.runtimex.tecmis.dao.NoticeDao;
import com.runtimex.tecmis.models.Notice;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoticeDaoTest {

    private static Connection conn;
    private static NoticeDao dao;

    private static final String TEST_ADMIN = "ADMIN_TEST_001";
    private static final String TEST_NOTICE_ID = "NOTICE_TEST_001";

    @BeforeAll
    static void setup() throws Exception {

        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tecmis_java",
                "root",
                "1234"
        );

        dao = new NoticeDao(conn);

        insertTestAdmin();
    }

    static void insertTestAdmin() throws Exception {
        conn.createStatement().execute("""
            INSERT IGNORE INTO users (id, f_name, l_name, email, contact_no, hash_pwd, user_type)
            VALUES ('ADMIN_TEST_001', 'Test', 'Admin', 'admin@test.com', '0700000000', 'hash', 'Admin')
        """);
    }

    @AfterEach
    void cleanup() throws Exception {
        conn.createStatement().execute(
                "DELETE FROM notice WHERE notice_id LIKE 'NOTICE_TEST_%'"
        );
    }

    @AfterAll
    static void close() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }

    // ---------------- Helper ----------------
    private Notice buildNotice(String id, String title) {
        Notice n = new Notice();
        n.setNoticeId(id);
        n.setTitle(title);
        n.setDate(new Date(System.currentTimeMillis()));
        return n;
    }

    // ---------------- CREATE ----------------
    @Test
    @Order(1)
    void testCreateNotice_Success() {

        Notice n = buildNotice(TEST_NOTICE_ID, "Exam Schedule Released");

        assertDoesNotThrow(() ->
                dao.createNotice(n, TEST_ADMIN)
        );
    }

    // ---------------- VIEW ALL ----------------
    @Test
    @Order(2)
    void testGetAllNotices_ReturnsInserted() {

        dao.createNotice(buildNotice(TEST_NOTICE_ID, "Notice 1"), TEST_ADMIN);

        List<Notice> list = dao.getAllNotices();

        assertNotNull(list);
        assertTrue(list.stream()
                .anyMatch(n -> n.getNoticeId().equals(TEST_NOTICE_ID)));
    }

    // ---------------- SEARCH ----------------
    @Test
    @Order(3)
    void testSearchNotices_FindsByKeyword() {

        dao.createNotice(buildNotice(TEST_NOTICE_ID, "Mid Exam Notice"), TEST_ADMIN);

        List<Notice> list = dao.searchNotices("Exam");

        assertFalse(list.isEmpty());
        assertTrue(list.stream()
                .anyMatch(n -> n.getTitle().contains("Exam")));
    }

    @Test
    @Order(4)
    void testSearchNotices_InvalidKeyword_ReturnsEmpty() {

        List<Notice> list = dao.searchNotices("NO_MATCH_123");

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    // ---------------- UPDATE ----------------
    @Test
    @Order(5)
    void testUpdateNotice_Success() {

        dao.createNotice(buildNotice(TEST_NOTICE_ID, "Old Title"), TEST_ADMIN);

        Notice updated = buildNotice(TEST_NOTICE_ID, "Updated Title");

        assertDoesNotThrow(() ->
                dao.updateNotice(updated, TEST_ADMIN)
        );

        List<Notice> list = dao.getAllNotices();

        Notice result = list.stream()
                .filter(n -> n.getNoticeId().equals(TEST_NOTICE_ID))
                .findFirst()
                .orElse(null);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    @Order(6)
    void testUpdateNotice_NotFound_ThrowsException() {

        Notice n = buildNotice("INVALID_ID", "Nothing");

        assertThrows(RuntimeException.class,
                () -> dao.updateNotice(n, TEST_ADMIN)
        );
    }

    // ---------------- DELETE ----------------
    @Test
    @Order(7)
    void testDeleteNotice_Success() {

        dao.createNotice(buildNotice(TEST_NOTICE_ID, "To Delete"), TEST_ADMIN);

        assertDoesNotThrow(() ->
                dao.deleteNotice(TEST_NOTICE_ID, TEST_ADMIN)
        );

        List<Notice> list = dao.getAllNotices();

        assertFalse(list.stream()
                .anyMatch(n -> n.getNoticeId().equals(TEST_NOTICE_ID)));
    }

    @Test
    @Order(8)
    void testDeleteNotice_NonExistent_NoException() {

        assertDoesNotThrow(() ->
                dao.deleteNotice("INVALID_ID", TEST_ADMIN)
        );
    }
}
