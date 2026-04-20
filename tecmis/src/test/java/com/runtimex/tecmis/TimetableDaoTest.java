package com.runtimex.tecmis;

import com.runtimex.tecmis.dao.TimetableDao;
import com.runtimex.tecmis.models.Timetable;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimetableDaoTest {

    private static Connection conn;
    private static TimetableDao dao;

    private static final String ADMIN_ID = "ADMIN001";
    private static final String TEST_ID = "TT_TEST_001";

    @BeforeAll
    static void setup() throws Exception {
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tecmis_java",
                "root",
                "1234"
        );

        dao = new TimetableDao(conn);

        // Insert test admin
        conn.createStatement().execute("""
            INSERT IGNORE INTO users (id, f_name, l_name, email, contact_no, hash_pwd, user_type)
            VALUES ('ADMIN001', 'Admin', 'Test', 'admin@test.com', '0700000000', 'hash', 'Admin')
        """);
    }

    @AfterEach
    void cleanup() throws Exception {
        conn.createStatement().execute("DELETE FROM timetable WHERE timetable_id LIKE 'TT_TEST_%'");
    }

    @AfterAll
    static void close() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }

    // Helper
    private Timetable buildSession(String id, String location) {
        Timetable t = new Timetable();
        t.setTimetableId(id);
        t.setLecturerId("LEC001");
        t.setCourseCode("ICT2142");
        t.setLocation(location);
        t.setLevel(2);
        t.setType("Lecture");
        t.setHours(2);
        return t;
    }

    // -------------------- ADD --------------------

    @Test
    @Order(1)
    void testAddSession_Success() {
        Timetable t = buildSession(TEST_ID, "Room A");
        assertDoesNotThrow(() -> dao.addSession(t, ADMIN_ID));
    }

    // -------------------- VIEW --------------------

    @Test
    @Order(2)
    void testViewTimetable_ReturnsInserted() {
        dao.addSession(buildSession(TEST_ID, "Room A"), ADMIN_ID);

        List<Timetable> list = dao.viewTimetable();

        assertTrue(list.stream().anyMatch(t -> t.getTimetableId().equals(TEST_ID)));
    }

    // -------------------- UPDATE --------------------

    @Test
    @Order(3)
    void testUpdateSession_Success() {
        dao.addSession(buildSession(TEST_ID, "Room A"), ADMIN_ID);

        Timetable updated = buildSession(TEST_ID, "Room B");
        dao.updateSession(updated, ADMIN_ID);

        Timetable result = dao.viewTimetable().stream()
                .filter(t -> t.getTimetableId().equals(TEST_ID))
                .findFirst()
                .orElse(null);

        assertNotNull(result);
        assertEquals("Room B", result.getLocation());
    }

    @Test
    @Order(4)
    void testUpdateSession_NotFound_ThrowsException() {
        Timetable t = buildSession("INVALID", "Room X");

        assertThrows(RuntimeException.class,
                () -> dao.updateSession(t, ADMIN_ID));
    }

    // -------------------- DELETE --------------------

    @Test
    @Order(5)
    void testDeleteSession_Success() {
        dao.addSession(buildSession(TEST_ID, "Room A"), ADMIN_ID);

        dao.deleteSession(TEST_ID, ADMIN_ID);

        boolean exists = dao.viewTimetable().stream()
                .anyMatch(t -> t.getTimetableId().equals(TEST_ID));

        assertFalse(exists);
    }

    @Test
    @Order(6)
    void testDeleteSession_NonExistent_NoException() {
        assertDoesNotThrow(() -> dao.deleteSession("INVALID", ADMIN_ID));
    }
}
