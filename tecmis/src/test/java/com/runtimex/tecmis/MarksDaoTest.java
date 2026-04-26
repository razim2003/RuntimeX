package com.runtimex.tecmis;

import com.runtimex.tecmis.dao.MarksDao;
import com.runtimex.tecmis.dao.impl.MarksDaoImpl;
import com.runtimex.tecmis.models.CourseExam;
import com.runtimex.tecmis.models.Mark;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MarksDaoTest {

    private static Connection conn;
    private static MarksDao dao;


    private static final String TEST_STU      = "TG/TST/0001";
    private static final String TEST_COURSE   = "ICT2142";
    private static final String TEST_TYPE     = "QU02";   // no seed row for 1781+ICT2142+QU02
    private static final String TEST_MARK_ID  = "TEST00000001";

    @BeforeAll
    static void setupConnection() throws Exception {
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tecmis_java",
                "root",
                "1234"
        );
        dao = new MarksDaoImpl(conn);
        insertTestStudent();
    }

    static void insertTestStudent() throws Exception {
        conn.createStatement().execute("""
        INSERT IGNORE INTO users (id, f_name, l_name, email, contact_no, hash_pwd, user_type)
        VALUES ('TG/TST/0001', 'Test', 'Student', 'test@test.lk', '0700000000', 'testhash', 'Undergraduate')
    """);
        conn.createStatement().execute("""
        INSERT IGNORE INTO undergraduate (stu_id, status)
        VALUES ('TG/TST/0001', 'Proper')
    """);
        conn.createStatement().execute("""
        INSERT IGNORE INTO enrollment (stu_id, course_code)
        VALUES ('TG/TST/0001', 'ICT2142')
    """);

    }


    @AfterAll
    static void closeConnection() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }
    //void insertTestStudent();


    @AfterEach
    void cleanup() throws Exception {
        conn.createStatement().execute("DELETE FROM marks WHERE mark_id LIKE 'TEST%'");
    }

    // -------------------------------------------------------
    // Helper
    // -------------------------------------------------------
    private Mark buildTestMark(String markId, double value) {
        CourseExam ce = new CourseExam();
        ce.setCourseCode(TEST_COURSE);
        ce.setExamTypeId(TEST_TYPE);
        return new Mark(markId, TEST_STU, ce, value);
    }


    // -------------------------------------------------------
    // addMark
    // -------------------------------------------------------

    @Test
    @Order(1)
    void testAddMark_Success() {
        Mark mark = buildTestMark(TEST_MARK_ID, 75.0);
        assertDoesNotThrow(() -> dao.addMark(mark));
    }

    @Test
    @Order(2)
    void testAddMark_DuplicateUniqueConstraint_ThrowsException() {
        Mark mark = buildTestMark(TEST_MARK_ID, 75.0);
        dao.addMark(mark);

        // same stu+course+type → violates UNIQUE(stu_id, course_code, type_id)
        Mark duplicate = buildTestMark("TEST00000002", 80.0);
        assertThrows(RuntimeException.class, () -> dao.addMark(duplicate));
    }

    @Test
    @Order(3)
    void testAddMark_InvalidEnrollment_ThrowsException() {
        CourseExam ce = new CourseExam();
        ce.setCourseCode("ICT2142");
        ce.setExamTypeId("QU02");

        // INVALID_STU has no enrollment → FK violation
        Mark mark = new Mark("TEST00000003", "INVALID_STU", ce, 50.0);
        assertThrows(RuntimeException.class, () -> dao.addMark(mark));
    }

    @Test
    @Order(4)
    void testAddMark_InvalidCourseExam_ThrowsException() {
        CourseExam ce = new CourseExam();
        ce.setCourseCode("INVALID");
        ce.setExamTypeId("ASST");

        Mark mark = new Mark("TEST00000004", TEST_STU, ce, 50.0);
        assertThrows(RuntimeException.class, () -> dao.addMark(mark));
    }


    // -------------------------------------------------------
    // getMark
    // -------------------------------------------------------

    @Test
    @Order(5)
    void testGetMark_ExistingSeededMark_ReturnsCorrectly() {
        // MK000000048: TG/2023/1781, ICT2142, ASST = 65.0 (from seed)
        Mark mark = dao.getMark("TG/2023/1781", "ICT2142", "ASST");

        assertNotNull(mark);
        assertEquals("TG/2023/1781", mark.getStudentId());
        assertEquals("ICT2142", mark.getCourseExam().getCourseCode());
        assertEquals("ASST", mark.getCourseExam().getExamTypeId());
        assertEquals(65.0, mark.getMark());
    }

    @Test
    @Order(6)
    void testGetMark_NonExistentReturnsNull() {
        Mark mark = dao.getMark("TG/TST/9999", "ICT2142", "QU02"); // no seed row, no test row
        assertNull(mark);
    }

    @Test
    @Order(7)
    void testGetMark_AfterAdd_ReturnsInsertedMark() {
        dao.addMark(buildTestMark(TEST_MARK_ID, 88.0));

        Mark fetched = dao.getMark(TEST_STU, TEST_COURSE, TEST_TYPE);
        assertNotNull(fetched);
        assertEquals(88.0, fetched.getMark());
        assertEquals(TEST_MARK_ID, fetched.getMarkId());
    }


    // -------------------------------------------------------
    // getMarksByStudentAndCourse
    // -------------------------------------------------------

    @Test
    @Order(8)
    void testGetMarksByStudentAndCourse_ReturnsSeededMarks() {
        // 1781 has seed marks for ICT2142: ASST(65), FIN(null row), QU01(70), QU03(68)
        // null-mark rows (FIN, QU02) were NOT inserted in seed, so expect 3 rows
        List<Mark> marks = dao.getMarksByStudentAndCourse("TG/2023/1781", "ICT2142");

        assertNotNull(marks);
        assertFalse(marks.isEmpty());
        marks.forEach(m -> {
            assertEquals("TG/2023/1781", m.getStudentId());
            assertEquals("ICT2142", m.getCourseExam().getCourseCode());
        });
    }

    @Test
    @Order(9)
    void testGetMarksByStudentAndCourse_InvalidStudent_ReturnsEmptyList() {
        List<Mark> marks = dao.getMarksByStudentAndCourse("INVALID_STU", "ICT2142");
        assertNotNull(marks);
        assertTrue(marks.isEmpty());
    }

    @Test
    @Order(10)
    void testGetMarksByStudentAndCourse_CountIncreasesAfterAdd() {
        List<Mark> before = dao.getMarksByStudentAndCourse(TEST_STU, TEST_COURSE);
        int countBefore = before.size();

        dao.addMark(buildTestMark(TEST_MARK_ID, 55.0));

        List<Mark> after = dao.getMarksByStudentAndCourse(TEST_STU, TEST_COURSE);
        assertEquals(countBefore + 1, after.size());
    }


    // -------------------------------------------------------
    // updateMark
    // -------------------------------------------------------

    @Test
    @Order(11)
    void testUpdateMark_Success() {
        dao.addMark(buildTestMark(TEST_MARK_ID, 60.0));

        Mark inserted = dao.getMark(TEST_STU, TEST_COURSE, TEST_TYPE);
        assertNotNull(inserted);

        inserted.setMark(95.0);
        dao.updateMark(inserted);

        Mark updated = dao.getMark(TEST_STU, TEST_COURSE, TEST_TYPE);
        assertEquals(95.0, updated.getMark());
    }

    @Test
    @Order(12)
    void testUpdateMark_NonExistentId_ThrowsException() {
        CourseExam ce = new CourseExam();
        ce.setCourseCode(TEST_COURSE);
        ce.setExamTypeId(TEST_TYPE);

        Mark ghost = new Mark("NONEXISTENT99", TEST_STU, ce, 50.0);
        assertThrows(RuntimeException.class, () -> dao.updateMark(ghost));
    }


    // -------------------------------------------------------
    // deleteMark
    // -------------------------------------------------------

    @Test
    @Order(13)
    void testDeleteMark_Success() {
        dao.addMark(buildTestMark(TEST_MARK_ID, 40.0));

        dao.deleteMark(TEST_MARK_ID);

        // verify by trying to fetch — should return null now
        Mark result = dao.getMark(TEST_STU, TEST_COURSE, TEST_TYPE);
        assertNull(result);
    }

    @Test
    @Order(14)
    void testDeleteMark_NonExistent_NoExceptionThrown() {
        // deleteMark doesn't verify rows affected, so should silently succeed
        assertDoesNotThrow(() -> dao.deleteMark("NONEXISTENT99"));
    }
}
